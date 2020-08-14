package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config.FieldName;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides a stack trace field in the JSON object.  This will add a field to the JSON object with a value derived from
 * any provided stack trace.
 * <p>
 * The default field name is <code>{@value #DEFAULT_FIELD_NAME}</code>.
 * <p>
 * The following XML fragments are equivalent:
 * <pre>{@code
 * <StackTrace>
 *   <FieldName>stack_trace</FieldName>
 * </StackTrace>}</pre>
 * and
 * <pre>{@code
 * <StackTrace/>}</pre>
 */
@Plugin(name = "StackTrace", category = Node.CATEGORY, elementType = "provider")
public final class StackTrace implements Provider {
    @Contract(value = "_ -> new", pure = true)
    @PluginFactory
    public static @NotNull StackTrace newStackTrace(@PluginElement("FieldName") final @Nullable FieldName fieldName) {
        return new StackTrace(fieldName == null ? DEFAULT_FIELD_NAME : fieldName.getFieldName());
    }

    private static final @NotNull String DEFAULT_FIELD_NAME = "stack_trace";


    private final @NotNull String fieldName;

    @Contract(pure = true)
    public @NotNull StackTrace(final @NotNull String fieldName) {
        this.fieldName = fieldName;
    }


    @Override
    @Contract(mutates = "param1")
    public void apply(final @NotNull ObjectNode line, final @NotNull LogEvent event) {
        final @Nullable ThrowableProxy thrown = event.getThrownProxy();
        if (thrown != null)
            line.put(
                    fieldName,
                    marshalStackTrace(thrown)
            );
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "Provider:StackTrace[fieldName=" + fieldName + ']';
    }


    @Contract(pure = true)
    private static @NotNull String marshalStackTrace(final @NotNull ThrowableProxy thrown) {
        final StringBuilder builder = new StringBuilder(2048);
        final Set<Throwable> exceptions = new HashSet<>();
        marshalStackTrace(thrown, builder, exceptions);
        return builder.toString();
    }

    @Contract(mutates = "param2,param3")
    private static void marshalStackTrace(
            final @NotNull ThrowableProxy thrown,
            final @NotNull StringBuilder builder,
            final @NotNull Set<Throwable> accumulatedExceptions
    ) {
        // Exception Summary
        builder.append(thrown.getName()).append(": ").append(thrown.getMessage()).append(System.lineSeparator());

        // Rest of trace
        for (final StackTraceElement line : thrown.getStackTrace()) {
            builder.append("\tat ").append(line.getClassName()).append('.').append(line.getMethodName())
                    .append('(').append(line.getFileName() == null ? "Unknown Source" : line.getFileName());
            if (line.getLineNumber() > 0) builder.append(':').append(line.getLineNumber());
            builder.append(')');
            builder.append(System.lineSeparator());
        }

        if (thrown.getCauseProxy() != null && !accumulatedExceptions.contains(thrown.getCauseProxy().getThrowable())) {
            accumulatedExceptions.add(thrown.getCauseProxy().getThrowable());
            builder.append("Caused by: ");
            marshalStackTrace(thrown.getCauseProxy(), builder, accumulatedExceptions);
        }
    }

}
