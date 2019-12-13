package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config.ExcludeMdcKeyNames;
import io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config.IncludeMdcKeyNames;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Plugin(name = "Mdc", category = Node.CATEGORY, elementType = "provider")
public final class Mdc implements Provider {
    @Contract(value = "_,_ -> new", pure = true)
    @PluginFactory
    public static @NotNull Mdc newMdc(
            @PluginElement("IncludeMdcKeyNames") final @Nullable IncludeMdcKeyNames includeMdcKeyNames,
            @PluginElement("ExcludeMdcKeyNames") final @Nullable ExcludeMdcKeyNames excludeMdcKeyNames
    ) {
        return new Mdc(
                (includeMdcKeyNames == null) ? Collections.emptySet() : includeMdcKeyNames.getKeys(),
                (excludeMdcKeyNames == null) ? Collections.emptySet() : excludeMdcKeyNames.getKeys()
        );
    }


    private final @NotNull Set<@NotNull String> includedKeyNames;
    private final @NotNull Set<@NotNull String> excludedKeyNames;


    @Contract(pure = true)
    private @NotNull Mdc(final @NotNull Set<@NotNull String> includedKeyNames,
                         final @NotNull Set<@NotNull String> excludedKeyNames
    ) {
        this.includedKeyNames = includedKeyNames;
        this.excludedKeyNames = excludedKeyNames;
    }

    @Override
    @Contract(mutates = "param1")
    public void apply(final @NotNull ObjectNode line, final @NotNull LogEvent event) {
        final @Nullable Map<@NotNull String, @Nullable String> contextMap = event.getContextData().toMap();
        if (contextMap == null)
            return;

        final Collection<String> keyNames = new HashSet<>(contextMap.keySet());
        keyNames.removeAll(excludedKeyNames);
        if (!includedKeyNames.isEmpty()) {
            keyNames.retainAll(includedKeyNames);
        }

        keyNames.forEach(key -> line.put(key, contextMap.get(key)));
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "Provider:Mdc";
    }

}
