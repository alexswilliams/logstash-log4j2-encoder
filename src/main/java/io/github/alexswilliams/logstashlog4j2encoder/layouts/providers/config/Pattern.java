package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config;

import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.Timestamp.PATTERN_UNIX_EPOCH_MILLIS_NUMBER;
import static io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.Timestamp.PATTERN_UNIX_EPOCH_MILLIS_STRING;

@Plugin(name = "Pattern", category = Node.CATEGORY, elementType = "TimestampPattern")
public final class Pattern {

    @PluginFactory
    @Contract(value = "null -> fail", pure = true)
    public static @NotNull Pattern newPattern(@Required @PluginValue("Pattern") final @NotNull String pattern) {
        Objects.requireNonNull(pattern);
        final String trimmedPattern = pattern.trim();
        if (!PATTERN_UNIX_EPOCH_MILLIS_NUMBER.equals(trimmedPattern)
                && !PATTERN_UNIX_EPOCH_MILLIS_STRING.equals(trimmedPattern)) {
            DateTimeFormatter.ofPattern(trimmedPattern);
        }
        return new Pattern(trimmedPattern);
    }

    private final @NotNull String pattern;

    @Contract(pure = true)
    private @NotNull Pattern(final @NotNull String pattern) {
        this.pattern = pattern;
    }

    @Contract(pure = true)
    public @NotNull String getPattern() {
        return this.pattern;
    }

}
