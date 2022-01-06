package project.logManager.service.model;


import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.logManager.common.utils.DateUtil;
import project.logManager.model.entity.User;
import project.logManager.model.respository.UserRepository;

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

    public String getBmiMessage(LocalDate geburtsDatum, Double gewicht, Double groesse) {
        Double bmi = berechneBMI(gewicht, groesse);
        int alterUser = getAgeFromBirthDate(geburtsDatum);

        if (alterUser < 18) {
            LOGGER.warn("Der User ist zu jung für eine genaue Bestimmung des BMI.");
            return "Der User ist zu jung für eine genaue Bestimmung des BMI.";
        }

        String bmiMessage = "Der User hat einen BMI von %s und ist somit";

        if (bmi > 18.5 && bmi <= 25) {
            return String.format(bmiMessage + " normalgewichtig.", bmi);
        } else if (bmi <= 18.5 && bmi > 0) {
            return String.format(bmiMessage + " untergewichtig.", bmi);
        } else if (bmi > 25) {
            return String.format(bmiMessage + " übergewichtig.", bmi);
        } else {
            LOGGER.error("Unexpected value");
            throw new IllegalStateException("Unexpected value");
        }
    }
    public Double berechneBMI(Double gewicht, Double groesse) {
        BigDecimal bigDecimal = new BigDecimal(gewicht / (groesse * groesse)).
                setScale(2, RoundingMode.DOWN);
        return bigDecimal.doubleValue();
    }

    public Double findUserAndCalculateBMI(String userName) {
        User user = userRepository.findUserByName(userName);
        return berechneBMI(user.getGewicht(), user.getGroesse());
    }
}
