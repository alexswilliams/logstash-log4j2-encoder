package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config;

import io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.Provider;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.status.StatusLogger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Plugin(name = "providers", category = Node.CATEGORY)
public class Providers {

    @NotNull
    public static final Providers DEFAULT_PROVIDERS = newBuilder()
            .setProviders(Provider.DEFAULT_PROVIDER_LIST)
            .build();

    @PluginBuilderFactory
    @SuppressWarnings("WeakerAccess")
    @Contract(value = " -> new", pure = true)
    public static <B extends Providers.Builder<B>> B newBuilder() {
        return new Providers.Builder<B>().asBuilder();
    }

    static class Builder<B extends Providers.Builder<B>>
            extends AbstractStringLayout.Builder<B>
            implements org.apache.logging.log4j.core.util.Builder<Providers> {

        private static final StatusLogger logger = StatusLogger.getLogger();

        @PluginElement("provider")
        @Nullable
        private Provider[] providers;

        @NotNull
        @Contract(value = "_ -> this", mutates = "this")
        B setProviders(final Provider[] providers) {
            this.providers = providers;
            logger.debug("Setting up Logstash JSON logger with following fields: " + Arrays.asList(providers).toString());

            final List<String> uniqueClassNames = Arrays.stream(providers)
                    .map(it -> it.getClass().getSimpleName())
                    .distinct()
                    .collect(Collectors.toList());

            if (uniqueClassNames.size() != providers.length) {
                logger.warn("Duplicate configuration nodes present for Logstash JSON encoder - results may be " +
                        "unpredictable.");
            }
            if (providers.length == 0) {
                logger.warn("An empty list of providers has been specified for logstash json encoder.");
            }

            return this.asBuilder();
        }

        @Override
        @NotNull
        @Contract(value = " -> new", pure = true)
        public Providers build() {
            return new Providers(
                    (providers == null || providers.length == 0)
                            ? Collections.emptyList()
                            : Arrays.asList(providers)
            );
        }
    }

    @NotNull
    final public List<@NotNull Provider> fields;

    @Contract(pure = true)
    @NotNull
    private Providers(@NotNull final List<@Nullable Provider> fields) {
        this.fields =
                Collections.unmodifiableList(
                        fields.stream()
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())
                );
    }
}
