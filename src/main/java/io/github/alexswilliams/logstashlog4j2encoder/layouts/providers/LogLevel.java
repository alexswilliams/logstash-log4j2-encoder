package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config.FieldName;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Provides a log level field in the JSON object.  This will add a field to the JSON object with a value derived from
 * the name of the log level, as specified within Log4j2's {@link Level} class.
 * <p>
 * The default field name is <code>{@value #DEFAULT_FIELD_NAME}</code>.
 * <p>
 * The following XML fragments are equivalent:
 * <pre>{@code
 * <LogLevel>
 *   <FieldName>level</FieldName>
 * </LogLevel>}</pre>
 * and
 * <pre>{@code
 * <LogLevel/>}</pre>
 */
@Plugin(name = "LogLevel", category = Node.CATEGORY, elementType = "provider")
public final class LogLevel implements Provider {
    @Contract(value = "_ -> new", pure = true)
    @PluginFactory
    public static @NotNull LogLevel newLogLevel(@PluginElement("FieldName") final @Nullable FieldName fieldName) {
        return new LogLevel(fieldName == null ? DEFAULT_FIELD_NAME : fieldName.getFieldName());
    }

    private static final @NotNull String DEFAULT_FIELD_NAME = "level";


    private final @NotNull String fieldName;

    @Contract(pure = true)
    public @NotNull LogLevel(final @NotNull String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    @Contract(mutates = "param1")
    public void apply(final @NotNull ObjectNode line, final @NotNull LogEvent event) {
        final @Nullable Level level = event.getLevel();
        if (level != null)
            line.put(
                    fieldName,
                    level.name()
            );
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "Provider:LogLevel[fieldName=" + this.fieldName + "]";
    }

}
