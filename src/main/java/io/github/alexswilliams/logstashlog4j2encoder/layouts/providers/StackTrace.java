package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Plugin(name = "StackTrace", category = Node.CATEGORY, elementType = "provider")
public final class StackTrace implements Provider {
    @Contract(value = " -> new", pure = true)
    @PluginFactory
    public static @NotNull StackTrace newStackTrace() {
        return new StackTrace();
    }

    @Contract(pure = true)
    private static @NotNull String marshalStackTrace(final @NotNull Throwable throwable) {
        final StringBuilder builder = new StringBuilder(throwable.getStackTrace().length * 100);
        for (final StackTraceElement line : throwable.getStackTrace()) {
            builder.append(line).append('\n');
        }
        return builder.toString();
    }

    @Override
    @Contract(mutates = "param1")
    public void apply(final @NotNull ObjectNode line, final @NotNull LogEvent event) {
        final @Nullable Throwable thrown = event.getThrown();
        if (thrown != null)
            line.put(
                    "stack_trace",
                    marshalStackTrace(thrown)
            );
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "Provider:StackTrace";
    }

}
