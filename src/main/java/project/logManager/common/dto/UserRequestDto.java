package project.logManager.common.dto;

import java.time.DateTimeException;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import project.logManager.common.message.ErrorMessages;
import project.logManager.exception.DateFormatException;
import project.logManager.service.model.UserService;

import java.time.DateTimeException;
import java.time.LocalDate;

import java.time.LocalDate;

import static project.logManager.common.dto.BmiRequestDto.getLocalDate;

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
    return getLocalDate(this.birthdate);
  }
}
