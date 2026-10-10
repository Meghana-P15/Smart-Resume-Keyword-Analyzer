package api;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA writes the SQL for us.
 * save(...) = INSERT, findAllByOrderByCreatedAtDesc() = SELECT ... ORDER BY created_at DESC
 */
public interface AnalysisRecordRepository extends JpaRepository<AnalysisRecord, Long> {

    List<AnalysisRecord> findAllByOrderByCreatedAtDesc();
}
