package project.logManager.service.validation;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import project.logManager.common.message.ErrorMessages;
import project.logManager.exception.ParameterNotPresentException;
import project.logManager.service.model.BmiService;
import project.logManager.service.model.UserService;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class BmiValidationService {
  private final BmiService bmiService;

  private static final Logger LOGGER = LogManager.getLogger(UserService.class);

  public void checkIfEntriesAreNull(LocalDate birthdate, Double weight, Double height) {
    if (birthdate == null || birthdate.equals("") || weight == null || height == null) {
      LOGGER.info(ErrorMessages.PARAMETER_IS_MISSING);
      throw new ParameterNotPresentException(ErrorMessages.PARAMETER_IS_MISSING);
    }
  }
}
