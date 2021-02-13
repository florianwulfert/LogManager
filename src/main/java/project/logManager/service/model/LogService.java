package project.logManager.service.model;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.logManager.common.enums.SeverityEnum;
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
        if (severity != null && message != null && startDate != null && endDate != null) {
            return logRepository.findBySeverityAndMessageContainingAndTimestampBetween(severity, message, startDate, endDate);
        }
        if (severity != null && message != null && startDate != null) {
            return logRepository.findBySeverityAndMessageContainingAndTimestampAfter(severity, message, startDate);
        }
        if (severity != null && message != null && endDate != null) {
            return logRepository.findBySeverityAndMessageContainingAndTimestampBefore(severity, message, endDate);
        }
        if (severity != null && message != null) {
            return logRepository.findBySeverityAndMessageContaining(severity, message);
        }
        if (severity != null && startDate != null && endDate != null) {
            return logRepository.findBySeverityAndTimestampBetween(severity, startDate, endDate);
        }
        if (severity != null && startDate != null) {
            return logRepository.findBySeverityAndTimestampAfter(severity, startDate);
        }
        if (severity != null && endDate != null) {
            return logRepository.findBySeverityAndTimestampBefore(severity, endDate);
        }
        if (severity != null) {
            return logRepository.findBySeverity(severity);
        }
        if (message != null && startDate != null && endDate != null) {
            return logRepository.findByMessageContainingAndTimestampBetween(message, startDate, endDate);
        }
        if (message != null && startDate != null) {
            return logRepository.findByMessageContainingAndTimestampAfter(message, startDate);
        }
        if (message != null && endDate != null) {
            return logRepository.findByMessageContainingAndTimestampBefore(message, endDate);
        }
        if (message != null) {
            return logRepository.findByMessageContaining(message);
        }
        if (startDate != null && endDate != null) {
            return logRepository.findByTimestampBetween(startDate, endDate);
        }
        if (startDate != null) {
            return logRepository.findByTimestampAfter(startDate);
        }
        if (endDate != null) {
            return logRepository.findByTimestampBefore(endDate);
        }
        return logRepository.findAll();
    }

    public void addLog(String message, String severity) {
        Log log = new Log();
        log.setMessage(message);
        log.setSeverity(severity);
        Date timestamp = new Date();
        log.setTimestamp(timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        logRepository.save(log);
    }

    public List<Log> getLogsBySeverity(String severity) {
        if (logValidationService.validateSeverity(severity)) {
            return logRepository.findBySeverity(severity);
        }
        LOGGER.error("Given severity '{}' is not allowed!", severity);
        throw new IllegalArgumentException("Given severity " + severity + " is not allowed!");
    }

    public List<Log> searchLogsByMessageParts(String message) {
        return logRepository.findByMessageContaining(message);
    }

    public List<Log> searchLogsByDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return logRepository.findByTimestampBetween(startDateTime, endDateTime);
    }
}
