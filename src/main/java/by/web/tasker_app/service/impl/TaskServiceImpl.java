package by.web.tasker_app.service.impl;

import by.web.tasker_app.dto.TaskDto;
import by.web.tasker_app.dto.TaskFilter;
import by.web.tasker_app.model.Task;
import by.web.tasker_app.repository.TaskRepository;
import by.web.tasker_app.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;


    @Override
    @Transactional(readOnly = true)
    public Task getTaskById(Long id) {
        log.debug("Fetching task with id: {}", id);
        return taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Task not found with id: {}", id);
                    return new EntityNotFoundException("Task not found with id: " + id);
                });
    }

    @Override
    @Transactional
    public Task createTask(TaskDto taskDto) {
        log.debug("Creating new task: {}", taskDto);
        Task task = modelMapper.map(taskDto, Task.class);
        Task savedTask = taskRepository.save(task);
        log.info("Created new task with id: {}", savedTask.getId());
        return savedTask;
    }

    @Override
    @Transactional
    public Task updateTask(Long id, TaskDto taskDto) {
        log.debug("Updating task with id: {}", id);
        Task existingTask = getTaskById(id);
        modelMapper.map(taskDto, existingTask);
        Task updatedTask = taskRepository.save(existingTask);
        log.info("Updated task with id: {}", id);
        return updatedTask;
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        log.debug("Deleting task with id: {}", id);
        if (!taskRepository.existsById(id)) {
            log.error("Task not found with id: {}", id);
            throw new EntityNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
        log.info("Deleted task with id: {}", id);
    }

    @Override
    public Page<Task> getTasks(TaskFilter filter) {
        log.debug("Getting tasks with filter: {}", filter);
        
        Sort sort = Sort.by(
            filter.getSortDirection().equalsIgnoreCase("ASC") ? 
                Sort.Direction.ASC : Sort.Direction.DESC,
            filter.getSortBy()
        );
        
        Pageable pageable = PageRequest.of(
            filter.getPage(),
            filter.getSize(),
            sort
        );
        
        Page<Task> tasks = taskRepository.findAllWithFilters(
            filter.getStatus(),
            filter.getSearch(),
            pageable
        );
        
        log.debug("Found {} tasks", tasks.getTotalElements());
        return tasks;
    }
} 