package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config;

import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Plugin(name = "IncludeMdcKeyNames", category = Node.CATEGORY)
public final class IncludeMdcKeyNames {

    @PluginFactory
    @Contract(pure = true)
    public static @NotNull IncludeMdcKeyNames newIncludeMdcKeyNames(@Required @PluginValue("IncludeMdcKeyNames") final @NotNull String keyNames) {
        Objects.requireNonNull(keyNames);
        final Set<String> keys = Arrays.stream(keyNames.split(","))
                .map(String::trim)
                .filter(it -> !it.isEmpty())
                .collect(Collectors.toSet());

        return new IncludeMdcKeyNames(keys);
    }

    private final @NotNull Set<@NotNull String> keys;

    @Contract(pure = true)
    private @NotNull IncludeMdcKeyNames(final @NotNull Set<@NotNull String> keys) {
        this.keys = keys;
    }

    @Contract(pure = true)
    public @NotNull Set<String> getKeys() {
        return Collections.unmodifiableSet(this.keys);
    }

}
