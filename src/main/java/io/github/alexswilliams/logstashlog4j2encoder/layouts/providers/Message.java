package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Plugin(name = "Message", category = Node.CATEGORY, elementType = "provider")
public class Message implements Provider {
    @PluginFactory
    @Contract(value = " -> new", pure = true)
    @NotNull
    public static Message newMessage() {
        return new Message();
    }


    @Override
    public void apply(@NotNull final ObjectNode line, @NotNull final LogEvent event) {
        line.put(
                "message",
                event.getMessage().getFormattedMessage()
        );
    }

    @Override
    @Contract(pure = true)
    @NotNull
    public String toString() {
        return "Provider:Message";
    }

}
