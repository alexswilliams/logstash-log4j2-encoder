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
import org.slf4j.MDC;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Plugin(name = "Mdc", category = Node.CATEGORY, elementType = "provider")
public class Mdc implements Provider {
    @PluginFactory
    @Contract(value = "_,_ -> new", pure = true)
    @NotNull
    public static Mdc newMdc(
            @PluginElement("IncludeMdcKeyNames") @Nullable final IncludeMdcKeyNames includeMdcKeyNames,
            @PluginElement("ExcludeMdcKeyNames") @Nullable final ExcludeMdcKeyNames excludeMdcKeyNames
    ) {
        return new Mdc(
                (includeMdcKeyNames == null) ? Collections.emptySet() : includeMdcKeyNames.getKeys(),
                (excludeMdcKeyNames == null) ? Collections.emptySet() : excludeMdcKeyNames.getKeys()
        );
    }


    @NotNull
    private final Set<@NotNull String> includedKeyNames;
    @NotNull
    private final Set<@NotNull String> excludedKeyNames;


    @NotNull
    @Contract(pure = true)
    private Mdc(@NotNull final Set<@NotNull String> includedKeyNames,
                @NotNull final Set<@NotNull String> excludedKeyNames
    ) {
        this.includedKeyNames = includedKeyNames;
        this.excludedKeyNames = excludedKeyNames;
    }

    @Override
    @Contract(mutates = "param1")
    public void apply(@NotNull final ObjectNode line, @NotNull final LogEvent event) {
        @Nullable final Map<@NotNull String, @Nullable String> contextMap = MDC.getCopyOfContextMap();
        if (contextMap == null)
            return;

        final Set<String> keyNames = contextMap.keySet();
        keyNames.removeAll(excludedKeyNames);
        if (!includedKeyNames.isEmpty()) {
            keyNames.retainAll(includedKeyNames);
        }

        keyNames.forEach(key -> line.put(key, contextMap.get(key)));
    }

    @Override
    @Contract(pure = true)
    @NotNull
    public String toString() {
        return "Provider:Mdc";
    }

}
