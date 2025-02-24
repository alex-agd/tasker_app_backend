package by.web.tasker_app.repository;

import by.web.tasker_app.BaseIntegrationTest;
import by.web.tasker_app.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void shouldSaveTask() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        
        Task savedTask = taskRepository.save(task);
        
        assertThat(savedTask.getId()).isNotNull();
        assertThat(savedTask.getTitle()).isEqualTo("Test Task");
    }
} 