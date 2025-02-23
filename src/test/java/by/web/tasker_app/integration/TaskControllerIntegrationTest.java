package by.web.tasker_app.integration;

import by.web.tasker_app.config.TestConfig;
import by.web.tasker_app.dto.TaskDto;
import by.web.tasker_app.model.Task;
import by.web.tasker_app.repository.TaskRepository;
import by.web.tasker_app.util.TestDataFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create new task")
    void createTask_Success() throws Exception {
        TaskDto taskDto = TestDataFactory.createTaskDto();

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(taskDto.getTitle())))
                .andExpect(jsonPath("$.description", is(taskDto.getDescription())))
                .andExpect(jsonPath("$.status", is(taskDto.getStatus())))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    @DisplayName("Should return bad request for invalid task data")
    void createTask_WithInvalidData_ReturnsBadRequest() throws Exception {
        TaskDto invalidTask = new TaskDto();
        invalidTask.setStatus("NEW");

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTask)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should get task by ID")
    void getTaskById_Success() throws Exception {
        Task savedTask = taskRepository.save(TestDataFactory.createTask());

        mockMvc.perform(get("/api/tasks/{id}", savedTask.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedTask.getId().intValue())))
                .andExpect(jsonPath("$.title", is(savedTask.getTitle())));
    }

    @Test
    @DisplayName("Should return not found for non-existent task")
    void getTaskById_NotFound() throws Exception {
        mockMvc.perform(get("/api/tasks/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should update existing task")
    void updateTask_Success() throws Exception {
        Task savedTask = taskRepository.save(TestDataFactory.createTask());
        TaskDto updateDto = TestDataFactory.createTaskDto();
        updateDto.setTitle("Updated Title");

        mockMvc.perform(put("/api/tasks/{id}", savedTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Title")));
    }

    @Test
    @DisplayName("Should delete existing task")
    void deleteTask_Success() throws Exception {
        Task savedTask = taskRepository.save(TestDataFactory.createTask());

        mockMvc.perform(delete("/api/tasks/{id}", savedTask.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/tasks/{id}", savedTask.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return bad request for invalid filter parameters")
    void getFilteredTasks_WithInvalidParams_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/tasks")
                        .param("status", "INVALID_STATUS")
                        .param("sortBy", "id")
                        .param("sortDirection", "ASC")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Invalid status")));
    }

    @Test
    @DisplayName("Should return empty page when no matches")
    void getFilteredTasks_NoMatches_ReturnsEmptyPage() throws Exception {
        mockMvc.perform(get("/api/tasks")
                        .param("search", "nonexistent")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements", is(0)))
                .andExpect(jsonPath("$.totalPages", is(0)));
    }

} 