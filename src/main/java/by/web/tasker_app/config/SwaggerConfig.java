package by.web.tasker_app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url(contextPath).description("Default Server URL")
                ))
                .info(new Info()
                        .title("Task Management API")
                        .description("REST API for managing tasks")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Your Name")
                                .email("test@gmail.com")));
    }
} 