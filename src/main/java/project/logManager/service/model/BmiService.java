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
            LOGGER.warn("User is too young for an exact definition of the BMI.");
            return "User is too young for an exact definition of the BMI.";
        }

        String bmiMessage = "User has a BMI of %s and therewith he has";

        if (bmi > 18.5 && bmi <= 25) {
            return String.format(bmiMessage + " normal weight.", bmi);
        } else if (bmi <= 18.5 && bmi > 0) {
            return String.format(bmiMessage + " underweight.", bmi);
        } else if (bmi > 25) {
            return String.format(bmiMessage + " overweight.", bmi);
        } else {
            LOGGER.error("BMI could not be calculated.");
            throw new IllegalStateException("BMI could not be calculated.");
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
