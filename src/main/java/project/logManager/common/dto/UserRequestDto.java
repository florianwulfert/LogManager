package project.logManager.common.dto;

import java.time.DateTimeException;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import project.logManager.common.message.ErrorMessages;
import project.logManager.exception.DateFormatException;

@Builder
@Data
public class UserRequestDto {
  public String actor;
  public String name;
  public String birthdate;
  public Double weight;
  public Double height;
  public String favouriteColor;

  public LocalDate getBirthdateAsLocalDate() {
    try {
      String[] bd = this.birthdate.split("-");
      return LocalDate.of(
          Integer.parseInt(bd[0]), Integer.parseInt(bd[1]), Integer.parseInt(bd[2]));
    } catch (IndexOutOfBoundsException | NumberFormatException | DateTimeException e) {
      throw new DateFormatException(ErrorMessages.ILLEGAL_BIRTHDATE_FORMAT);
    }
  }
}
