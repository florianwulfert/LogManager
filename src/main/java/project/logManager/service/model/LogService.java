package project.logManager.service.model;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.logManager.model.entity.Log;
import project.logManager.model.entity.User;
import project.logManager.model.respository.LogRepository;
import project.logManager.model.respository.UserRepository;
import project.logManager.service.validation.ValidationService;

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
@RequiredArgsConstructor
public class LogService {
    private static final Logger LOGGER = LogManager.getLogger(LogService.class);

    private final LogRepository logRepository;
    private final ValidationService logValidationService;
    private final UserRepository userRepository;

    public List<Log> getLogs(String severity, String message, LocalDateTime startDate, LocalDateTime endDate) {
        return logRepository.findLogs(severity, message, startDate, endDate);
    }

    public String addLog(String message, String severity, String userName) {
        String returnMessage = "";
        if (message != null && severity != null) {
            if (logValidationService.validateSeverity(severity)) {
                if (message.equals("Katze")) {
                    message = "Hund";
                    returnMessage = "Katze wurde in Hund übersetzt!\n";
                }

                returnMessage = returnMessage + String.format("Es wurde die Nachricht \"%s\" als %s abgespeichert!",
                        message, severity);

                Log log = new Log();
                log.setMessage(message);
                log.setSeverity(severity);
                boolean isUserAvailable = !userRepository.findAll().isEmpty();
                User user = userRepository.findUserByName(userName);
                if (isUserAvailable && user == null) {
                    LOGGER.error(String.format("User %s nicht gefunden", userName));
                    throw new RuntimeException(String.format("User %s nicht gefunden", userName));
                }
                log.setUser(user);
                Date timestamp = new Date();
                log.setTimestamp(timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                logRepository.save(log);
            } else {
                LOGGER.error("Die übergebene severity '{}' ist nicht zugelassen!", severity);
                throw new IllegalArgumentException("Severity falsch!");
            }
        } else {
            LOGGER.error("Einer der benötigten Parameter wurde nicht übergeben!");
            throw new RuntimeException("Einer der benötigten Parameter wurde nicht übergeben!");
        }
        return returnMessage;
    }

    public Log searchLogsByID(Integer id) {
        return logRepository.findById(id).isPresent() ? logRepository.findById(id).get() : null;
    }

    public String deleteById(Integer id) {
        logRepository.deleteById(id);
        return String.format("Eintrag mit der ID %s wurde aus der Datenbank gelöscht", id);
    }

    public boolean existLogByActorId(User actor) {
        List<Log> logs = logRepository.findByUser(actor);
        return !logs.isEmpty();
    }

    public String deleteBySeverity(String severity) {
        List<Log> deletedLogs = logRepository.deleteBySeverity(severity);
        if (deletedLogs.isEmpty()) {
            return "Keine Einträge gefunden!";
        }

        StringBuilder sb = new StringBuilder();

        String iDs = "";

        for (Log log : deletedLogs) {
            iDs += log.getId();
            if (deletedLogs.lastIndexOf(log) < deletedLogs.size() - 1) {
                iDs += ", ";
            }
        }

        sb.append("Es wurden die Einträge mit den IDs ").append(iDs).append(" aus der Datenbank gelöscht");
        return sb.toString();
    }
}
