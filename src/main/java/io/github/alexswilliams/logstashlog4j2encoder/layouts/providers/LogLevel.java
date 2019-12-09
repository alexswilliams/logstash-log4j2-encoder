package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Plugin(name = "LogLevel", category = Node.CATEGORY, elementType = "provider")
public class LogLevel implements Provider {
    @PluginFactory
    @Contract(value = " -> new", pure = true)
    @NotNull
    public static LogLevel newLogLevel() {
        return new LogLevel();
    }


    @Override
    @Contract(mutates = "param1")
    public void apply(@NotNull final ObjectNode line, @NotNull final LogEvent event) {
        line.put(
                "level",
                event.getLevel().toString()
        );
    }

    @Override
    @Contract(pure = true)
    @NotNull
    public String toString() {
        return "Provider:LogLevel";
    }

}
