package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config.FieldName;
import io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config.Pattern;
import io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config.TimeZone;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Plugin(name = "Timestamp", category = Node.CATEGORY, elementType = "provider")
public class Timestamp implements Provider {

    @PluginFactory
    @Contract(value = "_,_,_ -> new", pure = true)
    @NotNull
    public static Timestamp newTimestamp(
            @PluginElement("FieldName") @Nullable final FieldName fieldName,
            @PluginElement("TimeZone") @Nullable final TimeZone timeZone,
            @PluginElement("TimestampPattern") @Nullable final Pattern pattern
    ) {
        @NotNull final Marshaller marshaller;
        if (pattern == null) {
            marshaller = Marshaller.FORMATTER;
        } else if (PATTERN_UNIX_EPOCH_MILLIS_NUMBER.equals(pattern.getPattern())) {
            marshaller = Marshaller.EPOCH_MILLIS_NUMBER;
        } else if (PATTERN_UNIX_EPOCH_MILLIS_STRING.equals(pattern.getPattern())) {
            marshaller = Marshaller.EPOCH_MILLIS_STRING;
        } else {
            marshaller = Marshaller.FORMATTER;
        }

        @NotNull final DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern((marshaller == Marshaller.FORMATTER && pattern != null)
                        ? pattern.getPattern()
                        : DEFAULT_PATTERN)
                .withZone((timeZone == null)
                        ? DEFAULT_TIME_ZONE
                        : timeZone.getTimeZone());

        return new Timestamp(
                (fieldName == null)
                        ? DEFAULT_FIELD_NAME
                        : fieldName.getFieldName(),
                formatter,
                marshaller
        );
    }

    private enum Marshaller {
        FORMATTER,
        EPOCH_MILLIS_NUMBER,
        EPOCH_MILLIS_STRING
    }

    private static final String DEFAULT_FIELD_NAME = "@timestamp";
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
    private static final ZoneId DEFAULT_TIME_ZONE = ZoneOffset.UTC;
    public static final String PATTERN_UNIX_EPOCH_MILLIS_NUMBER = "[UNIX_TIMESTAMP_AS_NUMBER]";
    public static final String PATTERN_UNIX_EPOCH_MILLIS_STRING = "[UNIX_TIMESTAMP_AS_STRING]";


    @NotNull
    private final String fieldName;
    @NotNull
    private final DateTimeFormatter formatter;
    @NotNull
    private final Marshaller marshaller;

    @Contract(pure = true)
    @NotNull
    private Timestamp(@NotNull final String fieldName,
                      @NotNull final DateTimeFormatter formatter,
                      @NotNull final Marshaller marshaller
    ) {
        this.fieldName = fieldName;
        this.formatter = formatter;
        this.marshaller = marshaller;
    }

    @Override
    @Contract(mutates = "param1")
    public void apply(@NotNull final ObjectNode line, @NotNull final LogEvent event) {
        switch (marshaller) {
            case FORMATTER:
                line.put(fieldName, marshalTimestampWithFormatter(event));
                break;
            case EPOCH_MILLIS_NUMBER:
                line.put(fieldName, marshalTimestampAsEpochMillis(event));
                break;
            case EPOCH_MILLIS_STRING:
                line.put(fieldName, marshalTimestampAsEpochMillisString(event));
                break;
        }

    }


    @NotNull
    @Contract(pure = true)
    private String marshalTimestampWithFormatter(@NotNull final LogEvent event) {
        @NotNull final Instant instant = Instant.ofEpochSecond(
                event.getInstant().getEpochSecond(),
                event.getInstant().getNanoOfSecond()
        );
        return formatter.format(instant);
    }

    @Contract(pure = true)
    private static long marshalTimestampAsEpochMillis(@NotNull final LogEvent event) {
        return event.getInstant().getEpochMillisecond();
    }

    @NotNull
    @Contract(pure = true)
    private static String marshalTimestampAsEpochMillisString(@NotNull final LogEvent event) {
        return Long.toString(marshalTimestampAsEpochMillis(event));
    }

    @Override
    @Contract(pure = true)
    @NotNull
    public String toString() {
        return "Provider:Timestamp["
                + "marshaller=" + this.marshaller.toString() + ","
                + "fieldName=" + this.fieldName
                + "]";
    }

}
