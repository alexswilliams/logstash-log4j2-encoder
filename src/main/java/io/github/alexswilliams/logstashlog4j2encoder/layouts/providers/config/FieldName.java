package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config;

import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Plugin(name = "FieldName", category = Node.CATEGORY)
public final class FieldName {

    @PluginFactory
    @Contract(value = "null -> fail", pure = true)
    public static @NotNull FieldName newFieldName(@Required @PluginValue("FieldName") final @NotNull String fieldName) {
        if (fieldName.trim().isEmpty()) throw new IllegalArgumentException("FieldName cannot be blank");
        return new FieldName(fieldName.trim());
    }

    private final @NotNull String fieldName;

    @Contract(pure = true)
    private @NotNull FieldName(final @NotNull String fieldName) {
        this.fieldName = fieldName;
    }

    @Contract(pure = true)
    public @NotNull String getFieldName() {
        return this.fieldName;
    }

}
