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
 * Provides a logger name field in the JSON object.
 * <p>
 * The default field name is <code>{@value #DEFAULT_FIELD_NAME}</code>.
 * <p>
 * The following XML fragments are equivalent:
 * <pre>{@code
 * <LoggerName>
 *   <FieldName>logger_name</FieldName>
 * </LoggerName>}</pre>
 * and
 * <pre>{@code
 * <LoggerName/>}</pre>
 */
@Plugin(name = "LoggerName", category = Node.CATEGORY, elementType = "provider")
public final class LoggerName implements Provider {
    @Contract(value = "_ -> new", pure = true)
    @PluginFactory
    public static @NotNull LoggerName newLoggerName(@PluginElement("FieldName") final @Nullable FieldName fieldName) {
        return new LoggerName(fieldName == null ? DEFAULT_FIELD_NAME : fieldName.getFieldName());
    }

    // TODO: Support abbreviation

    private static final @NotNull String DEFAULT_FIELD_NAME = "logger_name";
    private final @NotNull String fieldName;

    @Contract(pure = true)
    public @NotNull LoggerName(final @NotNull String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    @Contract(mutates = "param1")
    public void apply(final @NotNull ObjectNode line, final @NotNull LogEvent event) {
        final @Nullable String loggerName = event.getLoggerName();
        if (loggerName != null)
            line.put(
                    fieldName,
                    loggerName.trim()
            );
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "Provider:LoggerName[" +
                "fieldName=" + this.fieldName +
                "]";
    }
}
