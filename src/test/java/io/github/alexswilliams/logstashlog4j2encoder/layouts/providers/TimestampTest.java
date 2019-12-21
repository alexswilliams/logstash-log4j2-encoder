package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers;

import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.core.LogEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TimestampTest {

    private static final JsonNodeCreator nodeFactory = new JsonNodeFactory(false);


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
                final Timestamp timestampProvider = Timestamp.newTimestamp(null, null, null);
                final ObjectNode outputJsonNode = nodeFactory.objectNode();
                final LogEvent inputEvent = mock(LogEvent.class);
                when(inputEvent.getInstant()).thenReturn(new Log4j2Instant(now));

                timestampProvider.apply(outputJsonNode, inputEvent);

                final String actualTimestamp = outputJsonNode.findValue(Timestamp.DEFAULT_FIELD_NAME).asText();
                final String actualDatePart = actualTimestamp.split("T")[0];

                Assertions.assertEquals(nowAsIsoString, actualDatePart);
            }
        }
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
            return instant.getNano() % 1000;
        }

        @Override
        public void formatTo(final StringBuilder buffer) {
            throw new RuntimeException("Not implemented");
        }
    }

}