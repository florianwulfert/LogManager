package project.logManager.common.utils;

import java.time.LocalDate;

public class DateUtil {

  public Integer getAgeFromBirthDate(LocalDate birthdate) {
    return LocalDate.now().getYear() - birthdate.getYear();
  }
}
