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

import java.util.*;
import java.util.stream.Collectors;

@Plugin(name = "providers", category = Node.CATEGORY)
public final class Providers {

    public static final @NotNull Providers DEFAULT_PROVIDERS = newBuilder()
            .setProviders(Provider.DEFAULT_PROVIDER_LIST)
            .build();

    @SuppressWarnings("WeakerAccess")
    @PluginBuilderFactory
    @Contract(value = " -> new", pure = true)
    public static <B extends Providers.Builder<B>> B newBuilder() {
        return new Providers.Builder<B>().asBuilder();
    }

    @SuppressWarnings("ClassNameSameAsAncestorName")
    static class Builder<B extends Providers.Builder<B>>
            extends AbstractStringLayout.Builder<B>
            implements org.apache.logging.log4j.core.util.Builder<Providers> {

        private static final StatusLogger logger = StatusLogger.getLogger();

        @PluginElement("provider")
        private @Nullable Provider[] providers;

        @NotNull
        @Contract(value = "_ -> this", mutates = "this")
        B setProviders(final Provider[] providers) {
            this.providers = providers.clone();
            logger.debug("Setting up Logstash JSON logger with following fields: {}", Arrays.asList(providers));

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

        @Contract(value = " -> new", pure = true)
        @Override
        public @NotNull Providers build() {
            return new Providers(
                    (providers == null || providers.length == 0)
                            ? Collections.emptyList()
                            : Arrays.asList(providers)
            );
        }
    }

    public final @NotNull List<@NotNull Provider> fields;

    @Contract(pure = true)
    private @NotNull Providers(final @NotNull Collection<@Nullable Provider> fields) {
        this.fields =
                Collections.unmodifiableList(
                        fields.stream()
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())
                );
    }
}
