package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers;

import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config.FieldName;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("ConstantConditions")
class StackTraceTest {

    private static final JsonNodeCreator nodeFactory = new JsonNodeFactory(false);
    private static final String DEFAULT_FIELD_NAME = "stack_trace";

    @Nested
    class given_no_throwable_is_present {

        private final Throwable thrown = null;

        @Test
        public void then_no_field_is_added() {
            final StackTrace stackTraceProvider = StackTrace.newStackTrace(null);
            final ObjectNode outputJsonNode = nodeFactory.objectNode();
            final LogEvent inputEvent = mockLogEvent(thrown);

            stackTraceProvider.apply(outputJsonNode, inputEvent);

            Assertions.assertFalse(outputJsonNode.has(DEFAULT_FIELD_NAME));
        }
    }


    @Nested
    class given_an_unexceptional_stack_trace {
        private final Throwable thrown = new Throwable("Some single-depth throwable");

        @Nested
        class with_default_field_name {
            @Test
            public void then_field_is_added() {
                final StackTrace stackTraceProvider = StackTrace.newStackTrace(null);
                final ObjectNode outputJsonNode = nodeFactory.objectNode();
                final LogEvent inputEvent = mockLogEvent(thrown);

                stackTraceProvider.apply(outputJsonNode, inputEvent);

                Assertions.assertTrue(outputJsonNode.has(DEFAULT_FIELD_NAME));
                Assertions.assertTrue(outputJsonNode.get(DEFAULT_FIELD_NAME).isTextual());
            }
        }

        @Nested
        class with_custom_field_name {
            private static final String customFieldName = "some field name";

            @Test
            public void then_field_is_added() {
                final StackTrace stackTraceProvider = StackTrace.newStackTrace(FieldName.newFieldName(customFieldName));
                final ObjectNode outputJsonNode = nodeFactory.objectNode();
                final LogEvent inputEvent = mockLogEvent(thrown);

                stackTraceProvider.apply(outputJsonNode, inputEvent);

                Assertions.assertTrue(outputJsonNode.has(customFieldName));
                Assertions.assertTrue(outputJsonNode.get(customFieldName).isTextual());
            }
        }
    }


    @Nested
    class given_a_single_depth_throwable {
        private final Throwable thrown = new Throwable("Some single-depth throwable");

        @Test
        public void then_summary_line_starts_with_class_name_of_throwable_followed_by_colon() {
            // Plausibly this is important for scripts which parse stack traces
            final String actual = runTest(thrown);
            final String[] lines = actual.split(System.lineSeparator());
            Assertions.assertTrue(Arrays.stream(lines).anyMatch(it ->
                    it.startsWith(thrown.getClass().getName() + ": ")));
        }

        @Test
        public void then_summary_line_contains_throwable_message() {
            final String actual = runTest(thrown);
            Assertions.assertTrue(actual.split(System.lineSeparator(), 2)[0].contains(thrown.getMessage()));
        }

        @Test
        public void then_stack_trace_is_indented() {
            final String actual = runTest(thrown);
            final String[] lines = actual.split(System.lineSeparator());
            Assertions.assertEquals(
                    lines.length - throwableChain(thrown).size(),
                    Arrays.stream(actual.split(System.lineSeparator())).filter(it -> it.startsWith("\t")).count()
            );
        }

        @Test
        public void then_stack_trace_lines_conform_to_pattern() {
            final String actual = runTest(thrown);
            final String[] lines = actual.split(System.lineSeparator());
            Arrays.stream(lines).filter(it -> it.startsWith("\t")).forEach(it ->
                    Assertions.assertTrue(it.matches("^\tat [a-zA-Z0-9.$<>_-]+\\([a-zA-Z0-9._-]+(:[0-9]+)?\\)$"))
            );
        }

        @Test
        public void then_stack_trace_has_new_line_at_the_end() {
            final String actual = runTest(thrown);
            Assertions.assertEquals(actual, actual.trim() + System.lineSeparator());
        }
    }

    @Nested
    class given_a_multiple_depth_throwable {
        private final Throwable thrown =
                new Throwable("Wrapping throwable",
                        new Exception("Causing throwable",
                                new Error("Some error")));

        @Test
        public void then_summary_line_starts_with_class_name_of_wrapping_throwable_followed_by_colon() {
            final String actual = runTest(thrown);
            final String[] lines = actual.split(System.lineSeparator());

            throwableChain(thrown).forEach(throwable ->
                    Assertions.assertTrue(
                            Arrays.stream(lines)
                                    .filter(it -> !it.startsWith("\t"))
                                    .map(it -> it.replaceFirst("^Caused by: ", ""))
                                    .anyMatch(it -> it.startsWith(throwable.getClass().getName() + ": ")))
            );
        }

        @Test
        public void then_all_but_the_root_summary_line_begins_with_caused_by() {
            final String actual = runTest(thrown);
            final String[] lines = actual.split(System.lineSeparator());

            throwableChain(thrown).stream().skip(1).forEach(throwable ->
                    Assertions.assertTrue(
                            Arrays.stream(lines)
                                    .filter(it -> !it.startsWith("\t"))
                                    .anyMatch(it -> it.startsWith("Caused by: " + throwable.getClass().getName())))
            );
        }

        @Test
        public void then_summary_line_contains_throwable_message() {
            final String actual = runTest(thrown);
            final String[] lines = actual.split(System.lineSeparator());

            throwableChain(thrown).forEach(throwable ->
                    Assertions.assertTrue(
                            Arrays.stream(lines).filter(it -> !it.startsWith("\t"))
                                    .anyMatch(it -> it.contains(throwable.getMessage()))
                    ));
        }

        @Test
        public void then_stack_trace_is_indented() {
            final String actual = runTest(thrown);
            final String[] lines = actual.split(System.lineSeparator());
            Assertions.assertEquals(
                    lines.length - throwableChain(thrown).size(),
                    Arrays.stream(actual.split(System.lineSeparator())).filter(it -> it.startsWith("\t")).count()
            );
        }

        @Test
        public void then_stack_trace_lines_conform_to_pattern() {
            final String actual = runTest(thrown);
            final String[] lines = actual.split(System.lineSeparator());
            Arrays.stream(lines).filter(it -> it.startsWith("\t")).forEach(it ->
                    Assertions.assertTrue(it.matches("^\tat [a-zA-Z0-9.$<>_-]+\\([a-zA-Z0-9._-]+(:[0-9]+)?\\)$"))
            );
        }

        @Test
        public void then_stack_trace_has_new_line_at_the_end() {
            final String actual = runTest(thrown);
            Assertions.assertEquals(actual, actual.trim() + System.lineSeparator());
        }
    }

    private static String runTest(@Nullable final Throwable thrown) {
        final StackTrace stackTraceProvider = StackTrace.newStackTrace(null);
        final ObjectNode outputJsonNode = nodeFactory.objectNode();
        final LogEvent inputEvent = mockLogEvent(thrown);

        stackTraceProvider.apply(outputJsonNode, inputEvent);

        Assertions.assertTrue(outputJsonNode.has(DEFAULT_FIELD_NAME));
        Assertions.assertTrue(outputJsonNode.get(DEFAULT_FIELD_NAME).isTextual());
        return outputJsonNode.get(DEFAULT_FIELD_NAME).asText();
    }

    @NotNull
    private static LogEvent mockLogEvent(@Nullable final Throwable thrown) {
        final LogEvent inputEvent = mock(LogEvent.class);
        when(inputEvent.getThrown()).thenReturn(thrown);
        when(inputEvent.getThrownProxy()).thenReturn(thrown == null ? null : new ThrowableProxy(thrown));
        return inputEvent;
    }

    private static @NotNull List<@NotNull Throwable> throwableChain(@NotNull Throwable thrown) {
        final List<Throwable> returnVal = new ArrayList<>();
        Throwable currentThrowable = thrown;
        while (currentThrowable != null && !returnVal.contains(currentThrowable)) {
            returnVal.add(currentThrowable);
            currentThrowable = currentThrowable.getCause();
        }
        return returnVal;
    }

}
