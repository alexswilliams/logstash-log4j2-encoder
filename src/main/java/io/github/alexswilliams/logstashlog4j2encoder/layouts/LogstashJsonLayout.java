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
public class LogstashJsonLayout extends AbstractStringLayout {

    @Contract("_ -> new")
    @NotNull
    @PluginFactory
    public static LogstashJsonLayout newLayout(@PluginElement("providers") final Providers providers) {
        return new LogstashJsonLayout(
                ((providers == null) ? Providers.DEFAULT_PROVIDERS : providers).fields
        );
    }

    @NotNull
    private static final ObjectMapper mapper = new ObjectMapper();

    @NotNull
    @Contract(pure = true)
    private LogstashJsonLayout(@NotNull final List<Provider> providers) {
        super(StandardCharsets.UTF_8);
        this.providers = providers;
    }


    @NotNull
    private final List<@NotNull Provider> providers;


    @Override
    @NotNull
    @Contract(pure = true)
    public String getContentType() {
        return "application/json";
    }

    @Override
    @NotNull
    @Contract(pure = true)
    public String toSerializable(@NotNull final LogEvent logEvent) {
        @NotNull final JsonNode logMap = logEventToNode(logEvent);
        return logMap.toString() + '\n';
    }

    @NotNull
    private JsonNode logEventToNode(@NotNull final LogEvent logEvent) {
        @NotNull final ObjectNode logLine = mapper.getNodeFactory().objectNode();
        for (final Provider provider : providers)
            provider.apply(logLine, logEvent);
        return logLine;
    }


}
