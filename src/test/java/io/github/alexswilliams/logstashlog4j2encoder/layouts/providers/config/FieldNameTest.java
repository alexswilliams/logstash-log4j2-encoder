package io.github.alexswilliams.logstashlog4j2encoder.layouts.providers.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings({"ResultOfMethodCallIgnored", "ObviousNullCheck", "ConstantConditions"})
class FieldNameTest {

    @Nested
    class given_null_field_name {
        @Test
        public void fieldname_is_not_constructed() {
            Assertions.assertThrows(IllegalArgumentException.class, () -> FieldName.newFieldName(null));
        }
    }

    @Nested
    class given_empty_field_name {
        @Test
        public void fieldname_is_not_constructed() {
            Assertions.assertThrows(Exception.class, () -> FieldName.newFieldName(""));
        }
    }

    @Nested
    class given_blank_field_name {
        @Test
        public void fieldname_is_not_constructed() {
            Assertions.assertThrows(Exception.class, () -> FieldName.newFieldName("   "));
        }
    }

    @Nested
    class given_valid_field_name {

        private static final String name = "some field name";

        @Test
        public void fieldname_created_given_name() {
            final FieldName fieldName = FieldName.newFieldName(name);
            Assertions.assertEquals(name, fieldName.getFieldName());
        }
    }

    @Nested
    class given_valid_field_name_surrounded_with_whitespace {
        private static final String name = "   some whitespace surrounded field name  ";

        @Test
        public void fieldname_is_constructed_as_if_whitespace_were_not_there() {
            final FieldName fieldName = FieldName.newFieldName(name);
            Assertions.assertEquals(name.trim(), fieldName.getFieldName());
        }
    }

}