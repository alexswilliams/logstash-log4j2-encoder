package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config.FieldName;
import org.apache.logging.log4j.core.LogEvent;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LoggerNameTest {

    private static final JsonNodeCreator nodeFactory = new JsonNodeFactory(false);
    private static final String DEFAULT_FIELD_NAME = "logger_name";

    @Nested
    class given_logger_name_is_not_specified {

        @Test
        public void then_no_field_is_added() {
            final LoggerName loggerNameProvider = LoggerName.newLoggerName(null);
            final ObjectNode outputJsonNode = nodeFactory.objectNode();
            final LogEvent inputEvent = mock(LogEvent.class);
            when(inputEvent.getLoggerName()).thenReturn(null);

            loggerNameProvider.apply(outputJsonNode, inputEvent);

            Assertions.assertFalse(outputJsonNode.has(DEFAULT_FIELD_NAME));
        }
    }

    @Nested
    class given_a_valid_logger_name {

        private final class TestScenario {
            final String name;
            final String input;
            final String expected;

            private TestScenario(final String name, final String input, final String expected) {
                this.name = name;
                this.input = input;
                this.expected = expected;
            }
        }

        final String longString = String.join("", Collections.nCopies(2048, "x"));
        final TestScenario[] scenarios = {
                new TestScenario("Empty logger name", "", ""),
                new TestScenario("Blank logger name", "    ", ""),
                new TestScenario("Unqualified logger name", "Main", "Main"),
                new TestScenario("Qualified logger name",
                        "io.github.alexswilliams.Main", "io.github.alexswilliams.Main"),
                new TestScenario("Logger name with trim-able whitespace",
                        "  io.github.alexswilliams.Main  ", "io.github.alexswilliams.Main"),
                new TestScenario("Long logger name", longString, longString),
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


    private static JsonNode runTest(final String loggerName, final String fieldName) {
        final LoggerName loggerNameProvider = LoggerName.newLoggerName(
                fieldName == null ? null : FieldName.newFieldName(fieldName));
        final ObjectNode outputJsonNode = nodeFactory.objectNode();
        final LogEvent inputEvent = mock(LogEvent.class);
        when(inputEvent.getLoggerName()).thenReturn(loggerName);

        loggerNameProvider.apply(outputJsonNode, inputEvent);

        final String expectedFieldName = fieldName == null ? DEFAULT_FIELD_NAME : fieldName;
        Assertions.assertTrue(outputJsonNode.has(expectedFieldName),
                "Field '" + expectedFieldName + "' is missing from object; instead got " + outputJsonNode);
        return outputJsonNode.get(expectedFieldName);
    }

}