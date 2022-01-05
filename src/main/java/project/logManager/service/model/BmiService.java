package project.logManager.service.model;


import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Transactional
@Service
@RequiredArgsConstructor
public class BmiService {
    private final UserService userService;

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    public String berechneBmi(LocalDate geburtsDatum, Double gewicht, Double groesse) {
        Double bmi = userService.berechneBMI(gewicht, groesse);
        int alterUser = getAgeFromBirthDate(geburtsDatum);

        if (alterUser < 18) {
            LOGGER.warn("Der User ist zu jung für eine genaue Bestimmung des BMI.");
            return "Der User ist zu jung für eine genaue Bestimmung des BMI.";
        }

        if (bmi > 18.5 && bmi <= 25) {
            return String.format("Der User hat einen BMI von %s und ist somit normalgewichtig.", bmi);
        } else if (bmi <= 18.5 && bmi > 0) {
            return String.format("Der User hat einen BMI von %s und ist somit untergewichtig.", bmi);
        } else if (bmi > 25) {
            return String.format("Der User hat einen BMI von %s und ist somit übergewichtig.", bmi);
        } else {
            LOGGER.error("Unexpected value");
            throw new IllegalStateException("Unexpected value");
        }
    }

    public Integer getAgeFromBirthDate(LocalDate geburtsDatum) {
        return LocalDate.now().getYear() - geburtsDatum.getYear();
    }
}
