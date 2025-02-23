package by.web.tasker_app.util;

import by.web.tasker_app.dto.TaskDto;
import by.web.tasker_app.model.Task;

import java.time.LocalDateTime;

public class TestDataFactory {
    
    public static TaskDto createTaskDto() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Test Task");
        taskDto.setDescription("Test Description");
        taskDto.setStatus("NEW");
        return taskDto;
    }

    public static Task createTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus("NEW");
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        return task;
    }
} 