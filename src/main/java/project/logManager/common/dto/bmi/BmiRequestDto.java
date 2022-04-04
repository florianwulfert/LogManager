package project.logManager.common.dto.bmi;

import java.time.DateTimeException;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import project.logManager.common.message.ErrorMessages;
import project.logManager.exception.DateFormatException;
import project.logManager.exception.ParameterNotPresentException;

@Builder
@Data
public class BmiRequestDto {

  public String birthdate;
  public Double weight;
  public Double height;

  public static LocalDate getLocalDate(String birthdate) {
    try {
      String[] bd = birthdate.split("-");
      return LocalDate.of(
          Integer.parseInt(bd[0]), Integer.parseInt(bd[1]), Integer.parseInt(bd[2]));
    } catch (IndexOutOfBoundsException | NumberFormatException | DateTimeException e) {
      throw new DateFormatException(ErrorMessages.ILLEGAL_BIRTHDATE_FORMAT);
    } catch (RuntimeException ex) {
      throw new ParameterNotPresentException(ErrorMessages.PARAMETER_IS_MISSING);
    }
  }

  public LocalDate getBirthdateAsLocalDate() {
    return getLocalDate(this.birthdate);
  }
}
