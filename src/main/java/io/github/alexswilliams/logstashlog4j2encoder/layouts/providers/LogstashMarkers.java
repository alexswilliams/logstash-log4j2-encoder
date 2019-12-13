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
public final class LogstashMarkers implements Provider {
    @PluginFactory
    @Contract(value = " -> new", pure = true)
    public static @NotNull LogstashMarkers newLogstashMarkers() {
        return new LogstashMarkers();
    }

    @Override
    @Contract(mutates = "param1")
    public void apply(final @NotNull ObjectNode line, final @NotNull LogEvent event) {
        final @Nullable Marker marker = event.getMarker();
        if (marker instanceof LogstashMarker)
            appendMarker(line, marker);
    }

    @Contract(mutates = "param1")
    private static void appendMarker(final @NotNull ObjectNode line, final @NotNull Marker marker) {
        // TODO
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "Provider:LogstashMarkers";
    }

}
