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
    List<Log> findBySeverity(String severity);
    List<Log> findByMessageContaining(String message);
    List<Log> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
}
