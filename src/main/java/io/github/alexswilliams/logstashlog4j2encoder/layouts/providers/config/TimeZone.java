package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config;

import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;
import java.util.Objects;

@Plugin(name = "TimeZone", category = Node.CATEGORY)
public final class TimeZone {

    @PluginFactory
    @Contract(value = "null -> fail", pure = true)
    public static @NotNull TimeZone newTimeZone(@Required @PluginValue("TimeZone") final @NotNull String timeZone) {
        Objects.requireNonNull(timeZone);
        final ZoneId zoneId = ZoneId.of(timeZone.trim());
        return new TimeZone(zoneId);
    }

    private final @NotNull ZoneId timeZone;

    @Contract(pure = true)
    private @NotNull TimeZone(final @NotNull ZoneId timeZone) {
        this.timeZone = timeZone;
    }

    @Contract(pure = true)
    public @NotNull ZoneId getTimeZone() {
        return this.timeZone;
    }

}
