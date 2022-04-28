package project.userFeaturePortal.common.dto.bmi;

import lombok.Builder;
import lombok.Data;
import project.userFeaturePortal.common.message.ErrorMessages;
import project.userFeaturePortal.exception.DateFormatException;
import project.userFeaturePortal.exception.ParameterNotPresentException;

import java.time.DateTimeException;
import java.time.LocalDate;

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
      throw new ParameterNotPresentException();
    }
  }

  public LocalDate getBirthdateAsLocalDate() {
    return getLocalDate(this.birthdate);
  }
}
