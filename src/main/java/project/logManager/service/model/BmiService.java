package project.logManager.service.model;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.logManager.common.message.ErrorMessages;
import project.logManager.common.message.InfoMessages;
import project.logManager.common.utils.DateUtil;
import project.logManager.exception.UserNotFoundException;
import project.logManager.model.entity.User;
import project.logManager.model.repository.UserRepository;
import project.logManager.service.validation.BmiValidationService;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Transactional
@Service
@RequiredArgsConstructor
public class BmiService extends DateUtil {
  private final UserRepository userRepository;
  private final BmiValidationService bmiValidationService;

  private static final Logger LOGGER = LogManager.getLogger(UserService.class);

  public String calculateBmiAndGetBmiMessage(LocalDate birthdate, Double weight, Double height) {
    bmiValidationService.checkIfEntriesAreNull(weight, height);
    Double bmi = calculateBMI(weight, height);
    LOGGER.info(getBmiMessage(birthdate, bmi));
    return getBmiMessage(birthdate, bmi);
  }

  private String getBmiMessage(LocalDate birthdate, double bmi) {
    int ageUser = getAgeFromBirthDate(birthdate);
    if (ageUser < 18) {
      LOGGER.warn(ErrorMessages.USER_TOO_YOUNG);
      return ErrorMessages.USER_TOO_YOUNG;
    }
    String bmiMessage = InfoMessages.BMI_MESSAGE;

    if (bmi > 18.5 && bmi <= 25) {
      return String.format(bmiMessage + InfoMessages.NORMAL_WEIGHT, bmi);
    } else if (bmi <= 18.5 && bmi > 0) {
      return String.format(bmiMessage + InfoMessages.UNDERWEIGHT, bmi);
    } else if (bmi > 25) {
      return String.format(bmiMessage + InfoMessages.OVERWEIGHT, bmi);
    } else {
      LOGGER.error(ErrorMessages.COULD_NOT_CALCULATE);
      throw new IllegalStateException(ErrorMessages.COULD_NOT_CALCULATE);
    }
  }

  public Double calculateBMI(Double weight, Double height) {
    BigDecimal bigDecimal =
        new BigDecimal(weight / (height * height)).setScale(2, RoundingMode.DOWN);
    return bigDecimal.doubleValue();
  }

  public String findUserAndGetBMI(String userName) {
    User user = userRepository.findUserByName(userName);
    if (user == null) {
      LOGGER.warn(String.format(ErrorMessages.USER_NOT_FOUND_NAME, userName));
      throw new UserNotFoundException(userName);
    }
    return getBmiMessage(user.getBirthdate(), user.getBmi());
  }
}
