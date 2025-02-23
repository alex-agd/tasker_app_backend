package by.web.tasker_app.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class TaskFilterValidationTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should validate valid filter")
    void validateValidFilter() {
        TaskFilter filter = new TaskFilter();
        filter.setStatus("NEW");
        filter.setSortBy("createdAt");
        filter.setSortDirection("DESC");
        filter.setPage(0);
        filter.setSize(20);

        var violations = validator.validate(filter);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"INVALID", "PENDING", "DONE"})
    @DisplayName("Should fail validation for invalid status")
    void validateInvalidStatus(String status) {
        TaskFilter filter = new TaskFilter();
        filter.setStatus(status);

        var violations = validator.validate(filter);
        assertEquals(1, violations.size());
        assertEquals("Invalid status", violations.iterator().next().getMessage());
    }


    @Test
    @DisplayName("Should fail validation for negative page")
    void validateNegativePage() {
        TaskFilter filter = new TaskFilter();
        filter.setPage(-1);

        var violations = validator.validate(filter);
        assertEquals(1, violations.size());
        assertEquals("Page number cannot be negative", violations.iterator().next().getMessage());
    }
} 