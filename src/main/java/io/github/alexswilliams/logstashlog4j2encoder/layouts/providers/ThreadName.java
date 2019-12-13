package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Plugin(name = "ThreadName", category = Node.CATEGORY, elementType = "provider")
public final class ThreadName implements Provider {
    @Contract(value = " -> new", pure = true)
    @PluginFactory
    public static @NotNull ThreadName newThreadName() {
        return new ThreadName();
    }


    @Override
    @Contract(mutates = "param1")
    public void apply(final @NotNull ObjectNode line, final @NotNull LogEvent event) {
        final @Nullable String threadName = event.getThreadName();
        if (threadName != null)
            line.put(
                    "thread_name",
                    threadName
            );
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "Provider:ThreadName";
    }

}
