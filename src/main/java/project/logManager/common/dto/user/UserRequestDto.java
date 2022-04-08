package project.logManager.common.dto.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

import static project.logManager.common.dto.bmi.BmiRequestDto.getLocalDate;

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
