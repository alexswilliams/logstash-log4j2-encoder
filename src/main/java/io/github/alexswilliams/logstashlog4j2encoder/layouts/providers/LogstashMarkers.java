package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.alexswilliams.logstashlog4j2encoder.markers.LogstashMarker;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Plugin(name = "LogstashMarkers", category = Node.CATEGORY, elementType = "provider")
public class LogstashMarkers implements Provider {
    @Contract(value = " -> new", pure = true)
    @PluginFactory
    @NotNull
    public static LogstashMarkers newLogstashMarkers() {
        return new LogstashMarkers();
    }

    @Override
    @Contract(mutates = "param1")
    public void apply(@NotNull final ObjectNode line, @NotNull final LogEvent event) {
        @Nullable final Marker marker = event.getMarker();
        if (marker instanceof LogstashMarker)
            appendMarker(line, marker);
    }

    @Contract(mutates = "param1")
    private static void appendMarker(@NotNull final ObjectNode line, @NotNull final Marker marker) {
        // TODO
    }

    @Override
    @Contract(pure = true)
    @NotNull
    public String toString() {
        return "Provider:LogstashMarkers";
    }

}
