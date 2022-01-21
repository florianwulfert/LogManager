package project.logManager.service.validation;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.logManager.common.dto.LogMessageDto;
import project.logManager.common.enums.SeverityEnum;

/**
 * @author - EugenFriesen
 * 13.02.2021
 **/
@Service
@Data
public class LogValidationService {

    private static final Logger LOGGER = LogManager.getLogger(LogValidationService.class);

    public boolean validateSeverity(String severity) {
        if (severity == null) {
            return true;
        }
        for (SeverityEnum severityEnum : SeverityEnum.values()) {
            if (severity.equals(severityEnum.name())) {
                return true;
            }
        }
        return false;
    }

    public LogMessageDto validateMessage(String message) {
        if (message.equals("Katze")) {
            LOGGER.info("Katze was translated to Hund!");
            return LogMessageDto.builder()
                    .message("Hund")
                    .returnMessage("Katze was translated to Hund!\n")
                    .build();
        } return LogMessageDto.builder()
                .message(message)
                .returnMessage("")
                .build();
    }
}
