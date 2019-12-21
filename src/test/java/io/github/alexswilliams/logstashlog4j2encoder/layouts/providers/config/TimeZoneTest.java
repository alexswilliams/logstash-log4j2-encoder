package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.DateTimeException;

@SuppressWarnings({"ResultOfMethodCallIgnored", "ObviousNullCheck", "ConstantConditions"})
class TimeZoneTest {

    @Nested
    class given_null_time_zone {
        @Test
        public void timezone_is_not_constructed() {
            Assertions.assertThrows(IllegalArgumentException.class, () -> TimeZone.newTimeZone(null));
        }
    }

    @Nested
    class given_empty_time_zone {
        @Test
        public void timezone_is_not_constructed() {
            Assertions.assertThrows(Exception.class, () -> TimeZone.newTimeZone(""));
        }
    }

    @Nested
    class given_blank_time_zone {
        @Test
        public void timezone_is_not_constructed() {
            Assertions.assertThrows(Exception.class, () -> TimeZone.newTimeZone("   "));
        }
    }

    @Nested
    class given_valid_time_zone {

        @Nested
        class when_time_zone_is_utc {
            private static final String zone = "UTC";

            @Test
            public void timezone_created_with_utc_zoneid() {
                final TimeZone timeZone = TimeZone.newTimeZone(zone);
                Assertions.assertEquals(zone, timeZone.getTimeZone().getId());
            }
        }

        @Nested
        class when_time_zone_is_named {
            private static final String zone = "Europe/Helsinki";

            @Test
            public void timezone_created_with_named_zone_id() {
                final TimeZone timeZone = TimeZone.newTimeZone(zone);
                Assertions.assertEquals(zone, timeZone.getTimeZone().getId());
            }
        }

        @Nested
        class when_time_zone_is_numeric_offset {
            private static final String zone = "-05:25";

            @Test
            public void timezone_created_with_offset_zone_id() {
                final TimeZone timeZone = TimeZone.newTimeZone(zone);
                Assertions.assertEquals(zone, timeZone.getTimeZone().getId());
            }
        }
    }

    @Nested
    class given_valid_time_zone_surrounded_with_whitespace {
        private static final String zone = "   Europe/Helsinki  ";

        @Test
        public void timezone_is_constructed_as_if_whitespace_were_not_there() {
            final TimeZone timeZone = TimeZone.newTimeZone(zone);
            Assertions.assertEquals(zone.trim(), timeZone.getTimeZone().getId());
        }
    }

    @Nested
    class given_an_invalid_time_zone {
        private static final String zone = "Atlantis";

        @Test
        public void timezone_is_not_constructed() {
            Assertions.assertThrows(DateTimeException.class, () -> TimeZone.newTimeZone(zone));
        }
    }

}