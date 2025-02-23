package by.web.tasker_app.service;

import by.web.tasker_app.dto.TaskDto;
import by.web.tasker_app.dto.TaskFilter;
import by.web.tasker_app.model.Task;
import org.springframework.data.domain.Page;

public interface TaskService {
    Task createTask(TaskDto taskDto);
    Task getTaskById(Long id);
    Page<Task> getTasks(TaskFilter filter);
    Task updateTask(Long id, TaskDto taskDto);
    void deleteTask(Long id);
} 