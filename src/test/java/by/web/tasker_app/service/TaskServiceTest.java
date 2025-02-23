package by.web.tasker_app.service;

import by.web.tasker_app.dto.TaskDto;
import by.web.tasker_app.dto.TaskFilter;
import by.web.tasker_app.model.Task;
import by.web.tasker_app.repository.TaskRepository;
import by.web.tasker_app.service.impl.TaskServiceImpl;
import by.web.tasker_app.util.TestDataFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    
    @Mock
    private ModelMapper modelMapper;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskServiceImpl(taskRepository, modelMapper);
    }

    @Nested
    @DisplayName("Create Task Tests")
    class CreateTaskTests {
        @Test
        @DisplayName("Should successfully create task")
        void createTask_Success() {
            // Arrange
            TaskDto taskDto = TestDataFactory.createTaskDto();
            Task task = TestDataFactory.createTask();
            
            when(modelMapper.map(taskDto, Task.class)).thenReturn(task);
            when(taskRepository.save(any(Task.class))).thenReturn(task);

            // Act
            Task createdTask = taskService.createTask(taskDto);

            // Assert
            assertNotNull(createdTask);
            assertEquals(task.getTitle(), createdTask.getTitle());
            verify(taskRepository).save(any(Task.class));
        }
    }


    @Nested
    @DisplayName("Update Task Tests")
    class UpdateTaskTests {
        @Test
        @DisplayName("Should successfully update task")
        void updateTask_WhenExists_Success() {
            // Arrange
            TaskDto taskDto = TestDataFactory.createTaskDto();
            Task existingTask = TestDataFactory.createTask();
            
            when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
            when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

            // Act
            Task updatedTask = taskService.updateTask(1L, taskDto);

            // Assert
            assertNotNull(updatedTask);
            verify(taskRepository).findById(1L);
            verify(taskRepository).save(any(Task.class));
            verify(modelMapper).map(taskDto, existingTask);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when updating non-existent task")
        void updateTask_WhenNotExists_ThrowsException() {
            // Arrange
            TaskDto taskDto = TestDataFactory.createTaskDto();
            when(taskRepository.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(EntityNotFoundException.class, () -> taskService.updateTask(1L, taskDto));
            verify(taskRepository).findById(1L);
            verify(taskRepository, never()).save(any(Task.class));
        }
    }

    @Nested
    @DisplayName("Delete Task Tests")
    class DeleteTaskTests {
        @Test
        @DisplayName("Should successfully delete task")
        void deleteTask_WhenExists_Success() {
            // Arrange
            when(taskRepository.existsById(1L)).thenReturn(true);

            // Act
            taskService.deleteTask(1L);

            // Assert
            verify(taskRepository).existsById(1L);
            verify(taskRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when deleting non-existent task")
        void deleteTask_WhenNotExists_ThrowsException() {
            // Arrange
            when(taskRepository.existsById(1L)).thenReturn(false);

            // Act & Assert
            assertThrows(EntityNotFoundException.class, () -> taskService.deleteTask(1L));
            verify(taskRepository).existsById(1L);
            verify(taskRepository, never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("Get Tasks With Filters Tests")
    class GetTasksWithFiltersTests {
        
        @Test
        @DisplayName("Should return filtered tasks page")
        void getTasks_WithFilters_Success() {
            // Arrange
            TaskFilter filter = new TaskFilter();
            filter.setStatus("NEW");
            filter.setSearch("test");
            filter.setSortBy("createdAt");
            filter.setSortDirection("DESC");
            filter.setPage(0);
            filter.setSize(10);

            Page<Task> mockPage = new PageImpl<>(List.of(TestDataFactory.createTask()));
            
            when(taskRepository.findAllWithFilters(
                eq("NEW"),
                eq("test"),
                any(Pageable.class)
            )).thenReturn(mockPage);

            // Act
            Page<Task> result = taskService.getTasks(filter);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getContent().size());
            verify(taskRepository).findAllWithFilters(
                eq("NEW"),
                eq("test"),
                any(Pageable.class)
            );
        }

        @Test
        @DisplayName("Should return empty page when no matches")
        void getTasks_NoMatches_ReturnsEmptyPage() {
            // Arrange
            TaskFilter filter = new TaskFilter();
            filter.setSearch("nonexistent");
            
            Page<Task> emptyPage = new PageImpl<>(List.of());
            when(taskRepository.findAllWithFilters(
                isNull(),
                eq("nonexistent"),
                any(Pageable.class)
            )).thenReturn(emptyPage);

            // Act
            Page<Task> result = taskService.getTasks(filter);

            // Assert
            assertTrue(result.isEmpty());
            assertEquals(0, result.getTotalElements());
        }
    }
} 