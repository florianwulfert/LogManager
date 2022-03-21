package project.logManager.service.validation;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.logManager.common.dto.LogMessageDto;
import project.logManager.common.dto.LogRequestDto;
import project.logManager.common.enums.SeverityEnum;
import project.logManager.common.message.ErrorMessages;
import project.logManager.common.message.InfoMessages;
import project.logManager.exception.ParameterNotPresentException;

/** @author - EugenFriesen 13.02.2021 */
@Service
@Data
public class LogValidationService {

  private static final Logger LOGGER = LogManager.getLogger(LogValidationService.class);

  public void checkIfAnyEntriesAreNull(LogRequestDto allParameters) {
    if (allParameters.severity == null
        || allParameters.severity.equals("")
        || allParameters.message == null
        || allParameters.message.equals("")
        || allParameters.user == null
        || allParameters.user.equals("")) {
      LOGGER.info(ErrorMessages.PARAMETER_IS_MISSING);
      throw new ParameterNotPresentException(ErrorMessages.PARAMETER_IS_MISSING);
    }
  }

  public boolean validateSeverity(String severity) {
    for (SeverityEnum severityEnum : SeverityEnum.values()) {
      if (severity.equals(severityEnum.name())) {
        return true;
      }
    }
    return false;
  }

  public LogMessageDto validateMessage(String message) {
    if (message.equals("Katze")) {
      LOGGER.info(InfoMessages.KATZE_TO_HUND);
      return LogMessageDto.builder()
          .message("Hund")
          .returnMessage(InfoMessages.KATZE_TO_HUND + "\n")
          .build();
    }
    return LogMessageDto.builder().message(message).returnMessage("").build();
  }
}
