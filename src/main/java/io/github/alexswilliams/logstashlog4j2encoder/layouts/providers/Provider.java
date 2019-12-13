package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.core.LogEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Provider {
    @Contract(mutates = "param1")
    void apply(final @NotNull ObjectNode line, final @NotNull LogEvent event);

    @NotNull
    Provider[] DEFAULT_PROVIDER_LIST = {
            Message.newMessage(),
            LogLevel.newLogLevel(),
            StackTrace.newStackTrace(),
            LoggerName.newLoggerName(),
            Timestamp.newTimestamp(null, null, null)
    };
}

