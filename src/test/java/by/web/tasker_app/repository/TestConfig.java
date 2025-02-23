package by.web.tasker_app.repository;

import by.web.tasker_app.model.Task;
import by.web.tasker_app.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();

        // Create test tasks
        Task task1 = TestDataFactory.createTask();
        task1.setStatus("NEW");
        task1.setTitle("Important task");

        Task task2 = TestDataFactory.createTask();
        task2.setStatus("COMPLETED");
        task2.setTitle("Regular task");
        
        taskRepository.saveAll(List.of(task1, task2));
    }

    @Test
    @DisplayName("Should find tasks with filters")
    void findAllWithFilters_Success() {
        // Act
        Page<Task> result = taskRepository.findAllWithFilters(
            "NEW",
            "Important",
            PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"))
        );

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("Important task", result.getContent().get(0).getTitle());
    }

    @Test
    @DisplayName("Should return empty page when no matches")
    void findAllWithFilters_NoMatches_ReturnsEmptyPage() {
        // Act
        Page<Task> result = taskRepository.findAllWithFilters(
            "NEW",
            "nonexistent",
            PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"))
        );

        // Assert
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    @DisplayName("Should return all tasks when no filters applied")
    void findAllWithFilters_NoFilters_ReturnsAllTasks() {
        // Act
        Page<Task> result = taskRepository.findAllWithFilters(
            null,
            null,
            PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"))
        );

        // Assert
        assertEquals(2, result.getTotalElements());
    }
}

@TestConfiguration
public class TestConfig {
    @Bean(name = "testDataSource")
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder.create()
            .url("jdbc:h2:mem:testdb")
            .driverClassName("org.h2.Driver")
            .username("sa")
            .password("")
            .build();

    }
} 