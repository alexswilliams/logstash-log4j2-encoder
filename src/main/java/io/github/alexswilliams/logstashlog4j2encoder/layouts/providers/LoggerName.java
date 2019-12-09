package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Plugin(name = "LoggerName", category = Node.CATEGORY, elementType = "provider")
public class LoggerName implements Provider {
    @PluginFactory
    @Contract(value = " -> new", pure = true)
    @NotNull
    public static LoggerName newLoggerName() {
        return new LoggerName();
    }


    @Override
    @Contract(mutates = "param1")
    public void apply(@NotNull final ObjectNode line, @NotNull final LogEvent event) {
        @Nullable final String loggerName = event.getLoggerName();
        if (loggerName != null)
            line.put(
                    "logger_name",
                    loggerName
            );
    }

    @Override
    @Contract(pure = true)
    @NotNull
    public String toString() {
        return "Provider:LoggerName";
    }
}
