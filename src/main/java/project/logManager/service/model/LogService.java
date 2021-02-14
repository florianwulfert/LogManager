package project.logManager.service.model;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.logManager.model.entity.Log;
import project.logManager.model.respository.LogRepository;
import project.logManager.service.validation.LogValidationService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * @author - EugenFriesen
 * 12.02.2021
 **/

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class LogService {
    private static final Logger LOGGER = LogManager.getLogger(LogService.class);

    private final LogRepository logRepository;
    private final LogValidationService logValidationService;

    public List<Log> getLogs(String severity, String message, LocalDateTime startDate, LocalDateTime endDate) {
        return logRepository.findLogs(severity, message, startDate, endDate);
    }

    public void addLog(String message, String severity) {
        if (message != null && severity != null) {
            if (logValidationService.validateSeverity(severity)) {
                Log log = new Log();
                log.setMessage(message);
                log.setSeverity(severity);
                Date timestamp = new Date();
                log.setTimestamp(timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                logRepository.save(log);
            } else {
                LOGGER.error("Given severity '{}' is not allowed!", severity);
                throw new IllegalArgumentException("Illegal severity!");
            }
        } else {
            LOGGER.error("One of the input parameter was not given!");
            throw new RuntimeException("One of the input parameter was not given!");
        }
    }

    public List<Log> getLogsBySeverity(String severity) {
        return logRepository.findBySeverity(severity);
    }

    public List<Log> searchLogsByMessageParts(String message) {
        return logRepository.findByMessageContaining(message);
    }

    public List<Log> searchLogsByDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return logRepository.findByTimestampBetween(startDateTime, endDateTime);
    }
}
