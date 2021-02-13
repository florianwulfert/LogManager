package project.logManager.model.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.logManager.model.entity.Log;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author - EugenFriesen
 * 12.02.2021
 **/

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findBySeverityAndMessageContainingAndTimestampBetween(String severity, String message, LocalDateTime startDate, LocalDateTime endDate);
    List<Log> findBySeverityAndMessageContainingAndTimestampAfter(String severity, String message, LocalDateTime startDate);
    List<Log> findBySeverityAndMessageContainingAndTimestampBefore(String severity, String message, LocalDateTime endDate);
    List<Log> findByMessageContainingAndTimestampBetween(String message, LocalDateTime startDate, LocalDateTime endDate);
    List<Log> findByMessageContainingAndTimestampAfter(String message, LocalDateTime startDate);
    List<Log> findByMessageContainingAndTimestampBefore(String message, LocalDateTime endDate);
    List<Log> findBySeverityAndTimestampBetween(String severity, LocalDateTime startDate, LocalDateTime endDate);
    List<Log> findBySeverityAndTimestampAfter(String severity, LocalDateTime startDate);
    List<Log> findBySeverityAndTimestampBefore(String severity, LocalDateTime endDate);
    List<Log> findBySeverityAndMessageContaining(String severity, String message);
    List<Log> findBySeverity(String severity);
    List<Log> findByMessageContaining(String message);
    List<Log> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Log> findByTimestampAfter(LocalDateTime startDate);
    List<Log> findByTimestampBefore(LocalDateTime endDate);
}
