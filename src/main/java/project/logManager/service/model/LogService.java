package project.logManager.service.model;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.logManager.model.entity.Log;
import project.logManager.model.entity.User;
import project.logManager.model.respository.LogRepository;
import project.logManager.service.validation.LogValidationService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * @author - EugenFriesen
 * 12.02.2021
 **/

@Transactional
@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class LogService {
    private static final Logger LOGGER = LogManager.getLogger(LogService.class);

    private final LogRepository logRepository;
    private final LogValidationService logValidationService;

    public List<Log> getLogs(String severity, String message, LocalDateTime startDate, LocalDateTime endDate) {
        return logRepository.findLogs(severity, message, startDate, endDate);
    }

    public String addLog(String message, String severity, User userName) {
        String MainMessage = "";
        if (message != null && severity != null && userName != null) {
            if (logValidationService.validateSeverity(severity)) {
                if (message.equals("Katze")) {
                    message = "Hund";
                    MainMessage = "Katze wurde in Hund übersetzt!\n";
                }

                MainMessage = MainMessage + String.format("Die Nachricht wurde als %s abgespeichert!", severity);

                Log log = new Log();
                log.setMessage(message);
                log.setSeverity(severity);
                log.setUser(userName);
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
        return MainMessage;
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

    public Log searchLogsByID(Integer id) {
        return logRepository.findById(id).isPresent() ? logRepository.findById(id).get() : null;
    }

    public void deleteById(Integer id) {
        logRepository.deleteById(id);
    }

    public String deleteBySeverity(String severity) {
        List<Log> deletedLogs = logRepository.deleteBySeverity(severity);
        if (deletedLogs.isEmpty()) {
            return "Keine Einträge gefunden!";
        }

        StringBuilder sb = new StringBuilder();

        String isEmpty = deletedLogs.size() == 0 ? "" : " ";
        String message1 = "Es wurden die Einträge mit den IDs" + isEmpty;
        String message2 = " aus der Datenbank gelöscht";
        String iDs = "";

        for (Log log : deletedLogs) {
            iDs += log.getId();
            if (deletedLogs.lastIndexOf(log) < deletedLogs.size()-1) {
                iDs += ", ";
            }
        }

        sb.append(message1).append(iDs).append(message2);
        return sb.toString();
    }
}
