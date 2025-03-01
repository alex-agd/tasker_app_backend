package by.web.tasker_app.repository;

import by.web.tasker_app.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(value = "SELECT * FROM tasks t WHERE " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:search IS NULL OR LOWER(CAST(t.title AS TEXT)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(CAST(t.description AS TEXT)) LIKE LOWER(CONCAT('%', :search, '%')))",
            nativeQuery = true)
    Page<Task> findAllWithFilters(@Param("status") String status, @Param("search") String search, Pageable pageable);
} 