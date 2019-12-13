package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Plugin(name = "Message", category = Node.CATEGORY, elementType = "provider")
public final class Message implements Provider {
    @Contract(value = " -> new", pure = true)
    @PluginFactory
    public static @NotNull Message newMessage() {
        return new Message();
    }


    @Override
    public void apply(final @NotNull ObjectNode line, final @NotNull LogEvent event) {
        line.put(
                "message",
                event.getMessage().getFormattedMessage()
        );
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "Provider:Message";
    }

}
