package io.github.alexswilliams.logstashlog4j2encoder.layouts;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.Provider;
import io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config.Providers;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.List;


@Plugin(name = "LogstashJsonLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public final class LogstashJsonLayout extends AbstractStringLayout {

    @PluginFactory
    @Contract("_ -> new")
    public static @NotNull LogstashJsonLayout newLayout(@PluginElement("providers") final Providers providers) {
        return new LogstashJsonLayout(
                ((providers == null) ? Providers.DEFAULT_PROVIDERS : providers).fields
        );
    }

    private static final @NotNull ObjectMapper mapper = new ObjectMapper();

    @Contract(pure = true)
    private @NotNull LogstashJsonLayout(final @NotNull List<? extends Provider> providers) {
        super(StandardCharsets.UTF_8);
        this.providers = providers;
    }


    private final @NotNull List<? extends Provider> providers;


    @Contract(pure = true)
    @Override
    public @NotNull String getContentType() {
        return "application/json";
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toSerializable(final @NotNull LogEvent logEvent) {
        logEvent.getContextData();
        final @NotNull JsonNode logMap = logEventToNode(logEvent);
        return logMap.toString() + '\n';
    }

    private @NotNull JsonNode logEventToNode(final @NotNull LogEvent logEvent) {
        final @NotNull ObjectNode logLine = mapper.getNodeFactory().objectNode();
        for (final Provider provider : providers)
            provider.apply(logLine, logEvent);
        return logLine;
    }


}
