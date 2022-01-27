package project.logManager.service.model;


import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.logManager.common.utils.DateUtil;
import project.logManager.exception.UserNotFoundException;
import project.logManager.model.entity.User;
import project.logManager.model.repository.UserRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static project.logManager.common.message.Messages.*;

@Transactional
@Service
@RequiredArgsConstructor
public class BmiService extends DateUtil {
    private final UserRepository userRepository;

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    public String getBmiMessage(LocalDate birthdate, Double weight, Double height) {
        Double bmi = calculateBMI(weight, height);
        int ageUser = getAgeFromBirthDate(birthdate);

        if (ageUser < 18) {
            LOGGER.warn(USER_TOO_YOUNG);
            return USER_TOO_YOUNG;
        }

        String bmiMessage = BMI_MESSAGE;

        if (bmi > 18.5 && bmi <= 25) {
            return String.format(bmiMessage + NORMAL_WEIGHT, bmi);
        } else if (bmi <= 18.5 && bmi > 0) {
            return String.format(bmiMessage + UNDERWEIGHT, bmi);
        } else if (bmi > 25) {
            return String.format(bmiMessage + OVERWEIGHT, bmi);
        } else {
            LOGGER.error(COULD_NOT_CALCULATE);
            throw new IllegalStateException(COULD_NOT_CALCULATE);
        }
    }

    public Double calculateBMI(Double weight, Double height) {
        BigDecimal bigDecimal = new BigDecimal(weight / (height * height)).
                setScale(2, RoundingMode.DOWN);
        return bigDecimal.doubleValue();
    }

    public String findUserAndCalculateBMI(String userName) {
        User user = userRepository.findUserByName(userName);
        if (user == null) {
            throw new UserNotFoundException(userName);
        }
        return getBmiMessage(user.getBirthdate(), user.getWeight(), user.getHeight());
    }
}
