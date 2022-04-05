package project.logManager.service.validation;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.logManager.common.dto.log.LogMessageDto;
import project.logManager.common.dto.log.LogRequestDto;
import project.logManager.common.enums.SeverityEnum;
import project.logManager.common.message.ErrorMessages;
import project.logManager.common.message.InfoMessages;
import project.logManager.exception.ParameterNotPresentException;
import project.logManager.exception.SeverityNotFoundException;
import project.logManager.exception.UserNotFoundException;
import project.logManager.model.entity.User;
import project.logManager.model.repository.UserRepository;

/**
 * @author - EugenFriesen 13.02.2021
 */
@Service
@Data
public class LogValidationService {

  private static final Logger LOGGER = LogManager.getLogger(LogValidationService.class);

  private final UserRepository userRepository;

  public void checkIfAnyEntriesAreNull(LogRequestDto allParameters) {
    if (allParameters.severity == null
        || allParameters.severity.equals("")
        || allParameters.message == null
        || allParameters.message.equals("")
        || allParameters.user == null
        || allParameters.user.equals("")) {
      LOGGER.warn(ErrorMessages.PARAMETER_IS_MISSING);
      throw new ParameterNotPresentException(ErrorMessages.PARAMETER_IS_MISSING);
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

  public User checkActor(String userName) {
    try {
      User user = userRepository.findUserByName(userName);
      if (user == null) {
        LOGGER.warn(String.format(ErrorMessages.USER_NOT_FOUND_NAME, userName));
        throw new RuntimeException();
      }
      LOGGER.info(String.format(InfoMessages.USER_FOUND, userName));
      return user;
    } catch (RuntimeException ex) {
      LOGGER.warn(String.format(ErrorMessages.USER_NOT_FOUND_NAME, userName));
      throw new UserNotFoundException(userName);
    }
  }
}
