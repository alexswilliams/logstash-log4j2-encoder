package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config.FieldName;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LogLevelTest {

    private static final JsonNodeCreator nodeFactory = new JsonNodeFactory(false);
    private static final String DEFAULT_FIELD_NAME = "level";

    @Nested
    class given_log_level_is_not_specified {

        @Test
        public void then_no_field_is_added() {
            final LogLevel logLevelProvider = LogLevel.newLogLevel(null);
            final ObjectNode outputJsonNode = nodeFactory.objectNode();
            final LogEvent inputEvent = mock(LogEvent.class);
            when(inputEvent.getLevel()).thenReturn(null);

            logLevelProvider.apply(outputJsonNode, inputEvent);

            Assertions.assertFalse(outputJsonNode.has(DEFAULT_FIELD_NAME));
        }
    }


    @Nested
    class given_a_valid_log_level {

        private final class TestScenario {
            final String name;
            final Level input;
            final String expected;

            private TestScenario(final String name, final Level input, final String expected) {
                this.name = name;
                this.input = input;
                this.expected = expected;
            }
        }

        final String longString = String.join("", Collections.nCopies(2048, "x"));
        final TestScenario[] scenarios = {
                new TestScenario("OFF", Level.OFF, "OFF"),
                new TestScenario("FATAL", Level.FATAL, "FATAL"),
                new TestScenario("ERROR", Level.ERROR, "ERROR"),
                new TestScenario("WARN", Level.WARN, "WARN"),
                new TestScenario("INFO", Level.INFO, "INFO"),
                new TestScenario("DEBUG", Level.DEBUG, "DEBUG"),
                new TestScenario("TRACE", Level.TRACE, "TRACE"),
                new TestScenario("ALL", Level.ALL, "ALL"),
        };

        @Nested
        class with_default_field_name {
            @TestFactory
            public Collection<DynamicTest> correct_value_added_to_object() {
                return Arrays.stream(scenarios).map(scenario -> DynamicTest.dynamicTest(scenario.name, () -> {
                    final JsonNode node = runTest(scenario.input, null);
                    Assertions.assertEquals(scenario.expected, node.textValue());
                })).collect(Collectors.toList());
            }
        }

        @Nested
        class with_custom_field_name {
            private static final String fieldName = "custom field name";

            @TestFactory
            public Collection<DynamicTest> correct_value_added_to_object() {
                return Arrays.stream(scenarios).map(scenario -> DynamicTest.dynamicTest(scenario.name, () -> {
                    final JsonNode node = runTest(scenario.input, fieldName);
                    Assertions.assertEquals(scenario.expected, node.textValue());
                })).collect(Collectors.toList());
            }
        }
    }


    private static JsonNode runTest(final Level logLevel, final String fieldName) {
        final LogLevel logLevelProvider = LogLevel.newLogLevel(
                fieldName == null ? null : FieldName.newFieldName(fieldName));
        final ObjectNode outputJsonNode = nodeFactory.objectNode();
        final LogEvent inputEvent = mock(LogEvent.class);
        when(inputEvent.getLevel()).thenReturn(logLevel);

        logLevelProvider.apply(outputJsonNode, inputEvent);

        final String expectedFieldName = fieldName == null ? DEFAULT_FIELD_NAME : fieldName;
        Assertions.assertTrue(outputJsonNode.has(expectedFieldName),
                "Field '" + expectedFieldName + "' is missing from object; instead got " + outputJsonNode);
        return outputJsonNode.get(expectedFieldName);
    }
}