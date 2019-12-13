package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Plugin(name = "LogLevel", category = Node.CATEGORY, elementType = "provider")
public final class LogLevel implements Provider {
    @Contract(value = " -> new", pure = true)
    @PluginFactory
    public static @NotNull LogLevel newLogLevel() {
        return new LogLevel();
    }


    @Override
    @Contract(mutates = "param1")
    public void apply(final @NotNull ObjectNode line, final @NotNull LogEvent event) {
        line.put(
                "level",
                event.getLevel().toString()
        );
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "Provider:LogLevel";
    }

}
