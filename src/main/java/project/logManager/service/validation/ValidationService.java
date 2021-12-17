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
public class ValidationService {

    private static final Logger LOGGER = LogManager.getLogger(ValidationService.class);

    public boolean validateSeverity(String severity) {
        for(SeverityEnum severityEnum : SeverityEnum.values()) {
            if(severity.equals(severityEnum.name())) {
                return true;
            }
        }
        return false;
    }

    public boolean validateFarbenEnum(String userFarben) {
        for(UserFarbenEnum farbenEnum : UserFarbenEnum.values()) {
            if (userFarben.equals(farbenEnum.getFarbe())) {
                return true;
            }
        }
        return false;
    }
}
