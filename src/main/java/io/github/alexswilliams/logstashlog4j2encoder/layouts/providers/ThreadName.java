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
 * Provides a thread name field in the JSON object.
 * <p>
 * The default field name is <code>{@value #DEFAULT_FIELD_NAME}</code>.
 * <p>
 * The following XML fragments are equivalent:
 * <pre>{@code
 * <ThreadName>
 *   <FieldName>thread_name</FieldName>
 * </ThreadName>}</pre>
 * and
 * <pre>{@code
 * <ThreadName/>}</pre>
 */
@Plugin(name = "ThreadName", category = Node.CATEGORY, elementType = "provider")
public final class ThreadName implements Provider {
    @Contract(value = "_ -> new", pure = true)
    @PluginFactory
    public static @NotNull ThreadName newThreadName(@PluginElement("FieldName") final @Nullable FieldName fieldName) {
        return new ThreadName(fieldName == null ? DEFAULT_FIELD_NAME : fieldName.getFieldName());
    }

    private static final @NotNull String DEFAULT_FIELD_NAME = "thread_name";
    private final @NotNull String fieldName;

    @Contract(pure = true)
    public @NotNull ThreadName(final @NotNull String fieldName) {
        this.fieldName = fieldName;
    }


    @Override
    @Contract(mutates = "param1")
    public void apply(final @NotNull ObjectNode line, final @NotNull LogEvent event) {
        final @Nullable String threadName = event.getThreadName();
        if (threadName != null)
            line.put(
                    fieldName,
                    threadName.trim()
            );
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "Provider:ThreadName[" +
                "fieldName=" + fieldName +
                "]";
    }

}
