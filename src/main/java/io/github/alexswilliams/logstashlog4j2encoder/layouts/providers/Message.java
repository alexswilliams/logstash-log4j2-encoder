package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config.FieldName;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Provides a message field in the JSON object.
 * <p>
 * The default field name is <code>{@value #DEFAULT_FIELD_NAME}</code>.
 * <p>
 * The following XML fragments are equivalent:
 * <pre>{@code
 * <Message>
 *   <FieldName>message</FieldName>
 * </Message>}</pre>
 * and
 * <pre>{@code
 * <Message/>}</pre>
 */
@Plugin(name = "Message", category = Node.CATEGORY, elementType = "provider")
public final class Message implements Provider {
    @Contract(value = "_ -> new", pure = true)
    @PluginFactory
    public static @NotNull Message newMessage(@PluginElement("FieldName") final @Nullable FieldName fieldName) {
        return new Message(fieldName == null ? DEFAULT_FIELD_NAME : fieldName.getFieldName());
    }

    private static final @NotNull String DEFAULT_FIELD_NAME = "message";


    private final @NotNull String fieldName;

    @Contract(pure = true)
    public @NotNull Message(final @NotNull String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public void apply(final @NotNull ObjectNode line, final @NotNull LogEvent event) {
        final @Nullable org.apache.logging.log4j.message.Message message = event.getMessage();
        if (message != null) {
            final @Nullable String formattedMessage = message.getFormattedMessage();
            if (formattedMessage != null)
                line.put(
                        fieldName,
                        formattedMessage.trim()
                );
        }
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "Provider:Message[fieldName=" + this.fieldName + "]";
    }

}
