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
public class TimeZone {

    @NotNull
    @Contract(value = "null -> fail", pure = true)
    @PluginFactory
    public static TimeZone newTimeZone(@PluginValue("TimeZone") @Required @NotNull final String timeZone) {
        Objects.requireNonNull(timeZone);
        final ZoneId zoneId = ZoneId.of(timeZone.trim());
        return new TimeZone(zoneId);
    }

    @NotNull
    private final ZoneId timeZone;

    @Contract(pure = true)
    @NotNull
    private TimeZone(@NotNull final ZoneId timeZone) {
        this.timeZone = timeZone;
    }

    @NotNull
    @Contract(pure = true)
    public ZoneId getTimeZone() {
        return this.timeZone;
    }

}
