package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config.FieldName;
import org.apache.logging.log4j.core.LogEvent;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MessageTest {

    private static final JsonNodeCreator nodeFactory = new JsonNodeFactory(false);
    private static final String DEFAULT_FIELD_NAME = "message";

    @Nested
    class given_message_is_not_specified {

        @Test
        public void then_no_field_is_added() {
            final Message messageProvider = Message.newMessage(null);
            final ObjectNode outputJsonNode = nodeFactory.objectNode();
            final LogEvent inputEvent = mock(LogEvent.class);
            when(inputEvent.getMessage()).thenReturn(null);

            messageProvider.apply(outputJsonNode, inputEvent);

            Assertions.assertFalse(outputJsonNode.has(DEFAULT_FIELD_NAME));
        }
    }

    @Nested
    class given_a_null_message {
        @Test
        public void then_no_field_is_added() {
            final Message messageProvider = Message.newMessage(null);
            final ObjectNode outputJsonNode = nodeFactory.objectNode();
            final LogEvent inputEvent = mock(LogEvent.class);
            when(inputEvent.getMessage()).thenReturn(new Log4j2Message(null));

            messageProvider.apply(outputJsonNode, inputEvent);

            Assertions.assertFalse(outputJsonNode.has(DEFAULT_FIELD_NAME));
        }
    }

    @Nested
    class given_a_valid_message {

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
                new TestScenario("Empty message", "", ""),
                new TestScenario("Blank message", "    ", ""),
                new TestScenario("Unspectacular message", "Some message", "Some message"),
                new TestScenario("Message with trim-able whitespace", "  Some message  ", "Some message"),
                new TestScenario("Long message", longString, longString),
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


    // TODO: We might want to consider checking that naughty strings are well escaped in the json output - that might
    //   either be a job for here, or for a higher level integration test.


    private static JsonNode runTest(final String message, final String fieldName) {
        final Message messageProvider = Message.newMessage(
                fieldName == null ? null : FieldName.newFieldName(fieldName));
        final ObjectNode outputJsonNode = nodeFactory.objectNode();
        final LogEvent inputEvent = mock(LogEvent.class);
        when(inputEvent.getMessage()).thenReturn(new Log4j2Message(message));

        messageProvider.apply(outputJsonNode, inputEvent);

        final String expectedFieldName = fieldName == null ? DEFAULT_FIELD_NAME : fieldName;
        Assertions.assertTrue(outputJsonNode.has(expectedFieldName),
                "Field '" + expectedFieldName + "' is missing from object; instead got " + outputJsonNode);
        return outputJsonNode.get(expectedFieldName);
    }

    @SuppressWarnings({"SuspiciousGetterSetter", "SerializableDeserializableClassInSecureContext"})
    private static final class Log4j2Message implements org.apache.logging.log4j.message.Message {
        public static final Object[] ZERO_OBJECT_ARRAY = new Object[0];
        private final String message;

        private Log4j2Message(final String message) {
            this.message = message;
        }

        @Override
        public String getFormattedMessage() {
            return message;
        }

        @Override
        public String getFormat() {
            return message;
        }

        @Override
        public Object[] getParameters() {
            return ZERO_OBJECT_ARRAY;
        }

        @Override
        public @Nullable Throwable getThrowable() {
            return null;
        }
    }

}