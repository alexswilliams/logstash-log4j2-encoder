package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config;

import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;

import static io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.Timestamp.PATTERN_UNIX_EPOCH_MILLIS_NUMBER;
import static io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.Timestamp.PATTERN_UNIX_EPOCH_MILLIS_STRING;

@Plugin(name = "TimestampPattern", category = Node.CATEGORY, elementType = "TimestampPattern")
public final class TimestampPattern {

    @PluginFactory
    @Contract(pure = true)
    public static @NotNull TimestampPattern newPattern(
            @Required @PluginValue("TimestampPattern") final @NotNull String pattern
    ) {
        final String trimmedPattern = pattern.trim();
        if (trimmedPattern.isEmpty()) {
            throw new RuntimeException("Cannot accept empty timestamp pattern");
        }
        if (!PATTERN_UNIX_EPOCH_MILLIS_NUMBER.equals(trimmedPattern)
                && !PATTERN_UNIX_EPOCH_MILLIS_STRING.equals(trimmedPattern)) {
            DateTimeFormatter.ofPattern(trimmedPattern);
        }
        return new TimestampPattern(trimmedPattern);
    }

    private final @NotNull String pattern;

    @Contract(pure = true)
    private @NotNull TimestampPattern(final @NotNull String pattern) {
        this.pattern = pattern;
    }

    @Contract(pure = true)
    public @NotNull String getPattern() {
        return this.pattern;
    }

}
