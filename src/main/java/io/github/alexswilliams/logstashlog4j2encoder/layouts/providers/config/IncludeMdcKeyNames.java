package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config;

import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Plugin(name = "IncludeMdcKeyNames", category = Node.CATEGORY)
public class IncludeMdcKeyNames {

    @NotNull
    @Contract(value = "null -> fail", pure = true)
    @PluginFactory
    public static IncludeMdcKeyNames newIncludeMdcKeyNames(@PluginValue("IncludeMdcKeyNames") @Required @NotNull final String keyNames) {
        Objects.requireNonNull(keyNames);
        final Set<String> keys = Arrays.stream(keyNames.split(","))
                .map(String::trim)
                .filter(it -> !"".equals(it))
                .collect(Collectors.toSet());

        return new IncludeMdcKeyNames(keys);
    }

    @NotNull
    private final Set<@NotNull String> keys;

    @NotNull
    @Contract(pure = true)
    private IncludeMdcKeyNames(@NotNull final Set<@NotNull String> keys) {
        this.keys = keys;
    }

    @NotNull
    @Contract(pure = true)
    public Set<String> getKeys() {
        return this.keys;
    }

}
