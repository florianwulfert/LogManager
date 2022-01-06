package project.logManager.common.utils;

import java.time.LocalDate;

public class DateUtil {
    public Integer getAgeFromBirthDate(LocalDate geburtsDatum) {
        return LocalDate.now().getYear() - geburtsDatum.getYear();
    }
}
