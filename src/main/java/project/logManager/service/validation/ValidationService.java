package project.logManager.service.validation;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.logManager.common.dto.LogMessageDto;
import project.logManager.common.enums.SeverityEnum;
import project.logManager.common.enums.UserFarbenEnum;

/**
 * @author - EugenFriesen
 * 13.02.2021
 **/
@Service
@Data
public class ValidationService {

    private static final Logger LOGGER = LogManager.getLogger(ValidationService.class);

    public boolean validateSeverity(String severity) {
        for (SeverityEnum severityEnum : SeverityEnum.values()) {
            if (severity.equals(severityEnum.name())) {
                return true;
            }
        }
        return false;
    }

    public boolean validateFarbenEnum(String userFarben) {
        for (UserFarbenEnum farbenEnum : UserFarbenEnum.values()) {
            if (userFarben.equals(farbenEnum.getFarbe())) {
                return true;
            }
        }
        return false;
    }

    public LogMessageDto validateMessage(String message) {
        if (message.equals("Katze")) {
            LOGGER.info("Katze wurde in Hund übersetzt!");
            return LogMessageDto.builder()
                    .message("Hund")
                    .returnMessage("Katze wurde in Hund übersetzt!\n")
                    .build();
        } return LogMessageDto.builder()
                .message(message)
                .returnMessage("")
                .build();
    }
}
