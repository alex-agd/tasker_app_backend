package by.web.tasker_app.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskDtoValidationTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should validate valid TaskDto")
    void validateValidTaskDto() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Valid Title");
        taskDto.setDescription("Valid Description");
        taskDto.setStatus("NEW");

        var violations = validator.validate(taskDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation for empty title")
    void validateEmptyTitle() {
        TaskDto taskDto = new TaskDto();
        taskDto.setStatus("NEW");

        var violations = validator.validate(taskDto);
        assertEquals(1, violations.size());
        assertEquals("Title is required", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Should fail validation for too short title")
    void validateTooShortTitle() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Ab");
        taskDto.setStatus("NEW");

        var violations = validator.validate(taskDto);
        assertEquals(1, violations.size());
        assertEquals("Title must be between 3 and 100 characters", 
                violations.iterator().next().getMessage());
    }
} 