package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config.FieldName;
import io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config.Pattern;
import io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config.TimeZone;
import org.apache.logging.log4j.core.LogEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.*;

import java.time.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TimestampTest {

    private static final JsonNodeCreator nodeFactory = new JsonNodeFactory(false);
    private static final String DEFAULT_FIELD_NAME = "@timestamp";

    @Nested
    class given_an_unspectacular_point_in_time {
        private final Instant instant = LocalDateTime
                .of(2019, Month.MAY, 6, 9, 10, 11, 987_654_321)
                .toInstant(ZoneOffset.UTC);

        @Nested
        class when_no_specified_timezone {
            private static final String expected = "2019-05-06T09:10:11.987Z";

            @Test
            public void default_timestamp_is_iso_format_with_millisecond_precision_and_z_for_zone_offset() {
                final JsonNode node = runTestWithParams(instant, null, null, null);
                Assertions.assertEquals(expected, node.textValue());
            }
        }

        @Nested
        class when_zone_is_named {
            private static final String zone = "Australia/Perth";
            private static final String expected = "2019-05-06T17:10:11.987+08:00";

            @Test
            public void default_timestamp_has_numeric_offset_with_minutes() {
                final JsonNode node = runTestWithParams(instant, null, zone, null);
                Assertions.assertEquals(expected, node.textValue());
            }
        }

        @Nested
        class when_zone_is_offset {
            private static final String zone = "+02:30";
            private static final String expected = "2019-05-06T11:40:11.987+02:30";

            @Test
            public void default_timestamp_has_numeric_offset_with_minutes() {
                final JsonNode node = runTestWithParams(instant, null, zone, null);
                Assertions.assertEquals(expected, node.textValue());
            }
        }

        @Nested
        class and_a_non_default_field_name {
            private static final String fieldName = "some other field name";
            private static final String expectedValue = "2019-05-06T09:10:11.987Z";

            @Test
            public void timestamp_assigned_to_different_field_name() {
                final JsonNode node = runTestWithParams(instant, fieldName, null, null);
                Assertions.assertEquals(expectedValue, node.textValue());
            }

        }

        @Nested
        class and_a_non_default_pattern {

            @Nested
            class when_pattern_is_a_valid_datetime_format {
                // Example taken from $product at $company
                private static final String pattern = "D/HHmmss";
                private static final String expected = "126/091011";

                @Test
                public void timestamp_respects_pattern() {
                    final JsonNode node = runTestWithParams(instant, null, null, pattern);
                    Assertions.assertEquals(expected, node.textValue());
                }

            }

            @Nested
            class when_pattern_requests_epoch_millis_as_string {
                private static final String pattern = "[UNIX_TIMESTAMP_AS_STRING]";
                private static final String expected = "1557133811987";

                @Test
                public void timestamp_is_epoch_millis_as_text_node() {
                    final JsonNode node = runTestWithParams(instant, null, null, pattern);
                    Assertions.assertEquals(expected, node.textValue());
                }

            }

            @Nested
            class when_pattern_requests_epoch_millis_as_number {
                private static final String pattern = "[UNIX_TIMESTAMP_AS_NUMBER]";
                private static final long expected = 1557133811987L;

                @Test
                public void timestamp_is_epoch_millis_as_text_node() {
                    final JsonNode node = runTestWithParams(instant, null, null, pattern);
                    Assertions.assertEquals(expected, node.longValue());
                }

            }

        }
    }

    @Nested
    class given_the_new_year_stars_half_way_through_the_week {

        @Nested
        class when_now_is_the_beginning_of_that_week {

            private static final String nowAsIsoString = "2019-12-30";
            private final Instant now = LocalDate
                    .of(2019, Month.DECEMBER, 30)
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC);

            @Test
            public void then_the_default_format_shows_the_old_year() {
                final JsonNode node = runTestWithParams(now, null, null, null);

                final String actualDatePart = node.textValue().split("T")[0];
                Assertions.assertEquals(nowAsIsoString, actualDatePart);
            }
        }
    }

    private @NotNull JsonNode runTestWithParams(
            final @NotNull Instant instant,
            final @Nullable String fieldName,
            final @Nullable String zone,
            final @Nullable String pattern
    ) {
        final Timestamp timestampProvider =
                Timestamp.newTimestamp(
                        fieldName == null ? null : FieldName.newFieldName(fieldName),
                        zone == null ? null : TimeZone.newTimeZone(zone),
                        pattern == null ? null : Pattern.newPattern(pattern)
                );
        final ObjectNode outputJsonNode = nodeFactory.objectNode();
        final LogEvent inputEvent = mock(LogEvent.class);
        when(inputEvent.getInstant()).thenReturn(new Log4j2Instant(instant));

        timestampProvider.apply(outputJsonNode, inputEvent);

        final JsonNode node = outputJsonNode.findValue(fieldName == null ? DEFAULT_FIELD_NAME : fieldName);
        Assertions.assertNotNull(node, "Field was not found.");

        return node;
    }


    private static class Log4j2Instant implements org.apache.logging.log4j.core.time.Instant {
        private final Instant instant;

        public Log4j2Instant(final Instant instant) {this.instant = instant;}

        @Override
        public long getEpochSecond() {
            return instant.getEpochSecond();
        }

        @Override
        public int getNanoOfSecond() {
            return instant.getNano();
        }

        @Override
        public long getEpochMillisecond() {
            return instant.toEpochMilli();
        }

        @Override
        public int getNanoOfMillisecond() {
            return instant.getNano() % 1_000_000;
        }

        @Override
        public void formatTo(final StringBuilder buffer) {
            throw new RuntimeException("Not implemented");
        }
    }

}