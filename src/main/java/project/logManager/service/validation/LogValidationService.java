package project.logManager.service.validation;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.logManager.common.enums.SeverityEnum;
import project.logManager.common.enums.UserFarbenEnum;

/**
 * @author - EugenFriesen
 * 13.02.2021
 **/
@Service
@Data
public class LogValidationService {

    private static final Logger LOGGER = LogManager.getLogger(LogValidationService.class);

    public boolean validateSeverity(String severity) {
        for(SeverityEnum severityEnum : SeverityEnum.values()) {
            if(severity.equals(severityEnum.name())) {
                return true;
            }
        }
        return false;
    }

    public boolean validateFarbenEnum(UserFarbenEnum userFarben) {
        for(UserFarbenEnum farbenEnum : UserFarbenEnum.values()) {
            if (userFarben.equals(UserFarbenEnum.values())) {
                return true;
            }
        }
        return false;
    }
}
