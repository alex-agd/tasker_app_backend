package by.web.tasker_app.repository;

import by.web.tasker_app.BaseIntegrationTest;
import by.web.tasker_app.model.Task;
import by.web.tasker_app.model.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration-test")
class TaskRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void shouldSaveTaskInPostgres() {
        Task task = new Task();
        task.setTitle("Integration Test Task");
        task.setDescription("Integration Test Description");
        task.setStatus(TaskStatus.NEW);
        
        Task savedTask = taskRepository.save(task);
        
        assertThat(savedTask.getId()).isNotNull();
        assertThat(savedTask.getTitle()).isEqualTo("Integration Test Task");
        assertThat(savedTask.getStatus()).isEqualTo(TaskStatus.NEW);
    }
} 