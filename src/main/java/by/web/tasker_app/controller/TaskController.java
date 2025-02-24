package by.web.tasker_app.controller;

import by.web.tasker_app.aspect.LogExecutionTime;
import by.web.tasker_app.dto.TaskDto;
import by.web.tasker_app.dto.TaskFilter;
import by.web.tasker_app.model.Task;
import by.web.tasker_app.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Task Management", description = "APIs for managing tasks")
@Validated
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/{id}")
    @Operation(summary = "Get task by IDs", description = "Retrieves a specific task by its ID")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PostMapping
    @Operation(summary = "Create new task", description = "Creates a new task")
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskDto taskDto) {
        return new ResponseEntity<>(taskService.createTask(taskDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update task", description = "Updates an existing task")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.updateTask(id, taskDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task", description = "Deletes a task by its ID")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get tasks", description = "Get tasks with filtering and pagination")
    @LogExecutionTime
    public ResponseEntity<Page<Task>> getTasks(
            @RequestParam(required = false) @Pattern(regexp = "^(NEW|IN_PROGRESS|COMPLETED)$", message = "Invalid status") String status,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") @Pattern(regexp = "^(id|title|description|status|createdAt)$", message = "Invalid sort field") String sortBy,
            @RequestParam(defaultValue = "ASC") @Pattern(regexp = "^(ASC|DESC)$", message = "Sort direction must be ASC or DESC") String sortDirection,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        
        TaskFilter filter = new TaskFilter();
        filter.setStatus(status);
        filter.setSearch(search);
        filter.setSortBy(sortBy);
        filter.setSortDirection(sortDirection);
        filter.setPage(page);
        filter.setSize(size);
        
        return ResponseEntity.ok(taskService.getTasks(filter));
    }
} 