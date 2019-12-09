package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config;

import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Plugin(name = "FieldName", category = Node.CATEGORY)
public class FieldName {

    @NotNull
    @Contract(value = "null -> fail", pure = true)
    @PluginFactory
    public static FieldName newFieldName(@PluginValue("FieldName") @Required @NotNull final String fieldName) {
        Objects.requireNonNull(fieldName);
        if (fieldName.trim().equals("")) throw new IllegalArgumentException("FieldName cannot be blank");
        return new FieldName(fieldName.trim());
    }

    @NotNull
    private final String fieldName;

    @NotNull
    @Contract(pure = true)
    private FieldName(@NotNull final String fieldName) {
        this.fieldName = fieldName;
    }

    @NotNull
    @Contract(pure = true)
    public String getFieldName() {
        return this.fieldName;
    }

}
