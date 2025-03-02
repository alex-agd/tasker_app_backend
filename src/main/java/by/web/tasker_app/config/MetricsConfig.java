package by.web.tasker_app.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    private final MeterRegistry meterRegistry;

    @Autowired
    public MetricsConfig(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Bean
    public Counter tasksCreatedCounter() {
        return Counter.builder("tasker_tasks_total")
                .description("Total number of created tasks")
                .register(meterRegistry);
    }

    @Bean
    public Counter tasksCompletedCounter() {
        return Counter.builder("tasker_tasks_completed_total")
                .description("Total number of completed tasks")
                .register(meterRegistry);
    }

} 