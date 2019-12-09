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
public class Pattern {

    @NotNull
    @Contract(value = "null -> fail", pure = true)
    @PluginFactory
    public static Pattern newPattern(@PluginValue("Pattern") @Required @NotNull final String pattern) {
        Objects.requireNonNull(pattern);
        final String trimmedPattern = pattern.trim();
        if (!PATTERN_UNIX_EPOCH_MILLIS_NUMBER.equals(trimmedPattern)
                && !PATTERN_UNIX_EPOCH_MILLIS_STRING.equals(trimmedPattern)) {
            DateTimeFormatter.ofPattern(trimmedPattern);
        }
        return new Pattern(trimmedPattern);
    }

    @NotNull
    private final String pattern;

    @NotNull
    @Contract(pure = true)
    private Pattern(@NotNull final String pattern) {
        this.pattern = pattern;
    }

    @NotNull
    @Contract(pure = true)
    public String getPattern() {
        return this.pattern;
    }

}
