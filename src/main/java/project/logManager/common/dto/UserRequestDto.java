package project.logManager.common.dto;

import lombok.Builder;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import project.logManager.common.message.ErrorMessages;
import project.logManager.exception.DateFormatException;
import project.logManager.service.model.UserService;

import java.time.DateTimeException;
import java.time.LocalDate;

@Builder
@Data
public class UserRequestDto {
  private static final Logger LOGGER = LogManager.getLogger(UserService.class);

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
      LOGGER.info(ErrorMessages.ILLEGAL_BIRTHDATE_FORMAT);
      throw new DateFormatException(ErrorMessages.ILLEGAL_BIRTHDATE_FORMAT);
    }
  }
}
