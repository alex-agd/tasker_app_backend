package by.web.tasker_app.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class TaskFilter {
    @Pattern(regexp = "^(NEW|IN_PROGRESS|COMPLETED)$", message = "Invalid status")
    private String status;
    
    private String search;
    
    @Pattern(regexp = "^(id|title|description|status|createdAt)$", message = "Invalid sort field")
    private String sortBy = "id";
    
    @Pattern(regexp = "^(ASC|DESC)$", message = "Sort direction must be ASC or DESC")
    private String sortDirection = "ASC";
    
    @Min(value = 0, message = "Page number cannot be negative")
    private int page = 0;
    
    @Min(value = 1, message = "Page size must be greater than 0")
    private int size = 10;
} 