package project.logManager.model.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.logManager.model.entity.Log;
import project.logManager.model.entity.User;

/**
 * @author - EugenFriesen 12.02.2021
 */
@Repository
public interface LogRepository extends JpaRepository<Log, Integer> {

  @Query(
      "SELECT log FROM Log log"
          + " WHERE (:severity is null or log.severity = :severity)"
          + " AND (:message is null or log.message like concat('%',:message,'%'))"
          + " AND (:startDate is null or log.timestamp > :startDate)"
          + " AND (:endDate is null or log.timestamp < :endDate)")
  List<Log> findLogs(
      String severity, String message, LocalDateTime startDate, LocalDateTime endDate);

  List<Log> deleteBySeverity(String severity);

  List<Log> findByUser(User user);
}
