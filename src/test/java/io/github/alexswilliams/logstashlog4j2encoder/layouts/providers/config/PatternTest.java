package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config;

import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@SuppressWarnings({"ResultOfMethodCallIgnored", "ObviousNullCheck", "ConstantConditions"})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PatternTest {

    @Nested
    class given_null_format {
        @Test
        public void pattern_is_not_constructed() {
            Assertions.assertThrows(Exception.class, () -> Pattern.newPattern(null));
        }
    }

    @Nested
    class given_empty_format {
        @Test
        public void pattern_is_not_constructed() {
            Assertions.assertThrows(Exception.class, () -> Pattern.newPattern(""));
        }
    }

    @Nested
    class given_blank_format {
        @Test
        public void pattern_is_not_constructed() {
            Assertions.assertThrows(Exception.class, () -> Pattern.newPattern("   "));
        }
    }

    @Nested
    class given_valid_format {
        private class TestScenario {
            final String name;
            final String format;

            TestScenario(final String name, final String format) {
                this.name = name;
                this.format = format;
            }
        }

        @TestFactory
        public Collection<DynamicTest> test() {
            return Arrays.stream(new TestScenario[]{
                    new TestScenario("DateTimeFormatter Pattern", "yyyy-MM-dd'T'HH:mm:ss.SSS"),
                    new TestScenario("DateTimeFormatter Pattern with whitespace", "  yyyy-MM-dd'T'HH:mm:ss.SSS  "),
                    new TestScenario("EpochMillis as String", "[UNIX_TIMESTAMP_AS_STRING]"),
                    new TestScenario("EpochMillis as String with whitespace", "  [UNIX_TIMESTAMP_AS_STRING]  "),
                    new TestScenario("EpochMillis as Number", "[UNIX_TIMESTAMP_AS_NUMBER]"),
                    new TestScenario("EpochMillis as Number with whitespace", "  [UNIX_TIMESTAMP_AS_NUMBER]  ")
            }).map(scenario -> DynamicTest.dynamicTest(scenario.name, () -> {
                final Pattern pattern = Pattern.newPattern(scenario.format);
                Assertions.assertEquals(scenario.format.trim(), pattern.getPattern());
            })).collect(Collectors.toList());
        }
    }

    @Nested
    class given_an_invalid_format {
        private static final String format = "SOME-VERY-INVALID-FORMAT";

        @Test
        public void pattern_is_not_constructed() {
            Assertions.assertThrows(IllegalArgumentException.class, () -> Pattern.newPattern(format));
        }
    }


}