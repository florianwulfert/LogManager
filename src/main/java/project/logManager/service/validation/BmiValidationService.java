package project.logManager.service.validation;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import project.logManager.common.message.ErrorMessages;
import project.logManager.common.message.InfoMessages;
import project.logManager.exception.ParameterNotPresentException;
import project.logManager.service.model.UserService;

@Component
@RequiredArgsConstructor
public class BmiValidationService {

  private static final Logger LOGGER = LogManager.getLogger(UserService.class);

  public void checkIfEntriesAreNull(Double weight, Double height) {
    if (weight == null || height == null) {
      LOGGER.warn(ErrorMessages.PARAMETER_IS_MISSING);
      throw new ParameterNotPresentException(ErrorMessages.PARAMETER_IS_MISSING);
    }
    LOGGER.info(InfoMessages.PARAMETERS_ARE_VALID);
  }
}
