package project.userFeaturePortal.service.validation;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.userFeaturePortal.common.dto.log.LogMessageDto;
import project.userFeaturePortal.common.dto.log.LogRequestDto;
import project.userFeaturePortal.common.enums.SeverityEnum;
import project.userFeaturePortal.common.message.ErrorMessages;
import project.userFeaturePortal.common.message.InfoMessages;
import project.userFeaturePortal.exception.ParameterNotPresentException;
import project.userFeaturePortal.exception.SeverityNotFoundException;
import project.userFeaturePortal.model.repository.UserRepository;

/**
 * @author - EugenFriesen 13.02.2021
 */
@Service
@Data
public class LogValidationService {

  private static final Logger LOGGER = LogManager.getLogger(LogValidationService.class);

  private final UserRepository userRepository;

  public void checkIfAnyEntriesAreNull(LogRequestDto allParameters) {
    if (allParameters.addLogRequest.severity == null
        || allParameters.addLogRequest.severity.equals("")
        || allParameters.addLogRequest.message == null
        || allParameters.addLogRequest.message.equals("")
        || allParameters.user == null
        || allParameters.user.equals("")) {
      LOGGER.warn(ErrorMessages.PARAMETER_IS_MISSING);
      throw new ParameterNotPresentException();
    }
  }

  public void validateSeverity(String severity) {
    for (SeverityEnum severityEnum : SeverityEnum.values()) {
      if (severity.equals(severityEnum.name())) {
        LOGGER.debug(InfoMessages.SEVERITY_VALID);
        return;
      }
    }
    LOGGER.warn(ErrorMessages.SEVERITY_NOT_REGISTERED, severity);
    throw new SeverityNotFoundException(severity);
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
