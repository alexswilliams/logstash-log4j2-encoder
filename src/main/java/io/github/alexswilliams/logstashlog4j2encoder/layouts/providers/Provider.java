package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.core.LogEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Defines the minimum API for any field provider.
 * <p>
 * All implementations can expect to be called (in no particular order) for each log line presented to the encoder.
 * Their purpose is to mutate the {@code ObjectNode} given to them, enriching it with information derived from the
 * provided {@code LogEvent} object.
 * <p>
 * Providers are configured by specifying a {@code <Providers>...</Providers>} block within the XML configuration (or
 * equivalent,) and listing each provider by name.
 *
 * @see io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config.Providers
 */
@FunctionalInterface
public interface Provider {
    @Contract(mutates = "param1")
    void apply(final @NotNull ObjectNode line, final @NotNull LogEvent event);

    @NotNull
    Provider[] DEFAULT_PROVIDER_LIST = {
            Message.newMessage(null),
            LogLevel.newLogLevel(null),
            StackTrace.newStackTrace(),
            LoggerName.newLoggerName(),
            Timestamp.newTimestamp(null, null, null)
    };
}

