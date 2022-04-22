package project.userFeaturePortal.common.dto.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

import static project.userFeaturePortal.common.dto.bmi.BmiRequestDto.getLocalDate;

@Builder
@Data
public class UserRequestDto {

  public String actor;
  public String name;
  public String birthdate;
  public Double weight;
  public Double height;
  public String favouriteBook;

    public LocalDate getBirthdateAsLocalDate() {
    return getLocalDate(this.birthdate);
  }
}
