package by.web.tasker_app;

import by.web.tasker_app.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {TaskerAppApplication.class, TestConfig.class})
@ActiveProfiles("test")
class TaskerAppApplicationTests {

    @Test
    void contextLoads() {
    }

}
