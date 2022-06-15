package project.userFeaturePortal.service.model;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.userFeaturePortal.common.message.ErrorMessages;
import project.userFeaturePortal.exception.UserNotFoundException;
import project.userFeaturePortal.model.entity.User;
import project.userFeaturePortal.model.repository.UserRepository;
import project.userFeaturePortal.service.validation.BmiValidationService;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Transactional
@Service
@RequiredArgsConstructor
public class BmiService {

  private static final Logger LOGGER = LogManager.getLogger(BmiService.class);
  private final UserRepository userRepository;
  private final BmiValidationService bmiValidationService;

  public String calculateBmiAndGetBmiMessage(LocalDate birthdate, Double weight, Double height) {
    User user = new User();
    bmiValidationService.checkIfEntriesAreNull(weight, height);
    user.setHeight(height);
    user.setWeight(weight);
    user.setBmi(user.calculateBMI());
    user.setBirthdate(birthdate);
    LOGGER.info(user.getBmiMessage());
    return user.getBmiMessage();
  }

  public String findUserAndGetBMI(String userName) {
    User user = userRepository.findUserByName(userName);
    if (user == null) {
      LOGGER.warn(String.format(ErrorMessages.USER_NOT_FOUND_NAME, userName));
      throw new UserNotFoundException(userName);
    }
    return user.getBmiMessage();
  }
}
