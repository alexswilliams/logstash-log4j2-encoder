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
public class StackTrace implements Provider {
    @PluginFactory
    @Contract(value = " -> new", pure = true)
    @NotNull
    public static StackTrace newStackTrace() {
        return new StackTrace();
    }

    @NotNull
    @Contract(pure = true)
    private static String marshalStackTrace(@NotNull final Throwable throwable) {
        final StringBuilder builder = new StringBuilder();
        for (final StackTraceElement line : throwable.getStackTrace()) {
            builder.append(line.toString()).append('\n');
        }
        return builder.toString();
    }

    @Override
    @Contract(mutates = "param1")
    public void apply(@NotNull final ObjectNode line, @NotNull final LogEvent event) {
        @Nullable final Throwable thrown = event.getThrown();
        if (thrown != null)
            line.put(
                    "stack_trace",
                    marshalStackTrace(thrown)
            );
    }

    @Override
    @Contract(pure = true)
    @NotNull
    public String toString() {
        return "Provider:StackTrace";
    }

}
