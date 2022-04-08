package project.logManager.common.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import project.logManager.model.entity.Book;

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
  public Book favouriteBook;

  public LocalDate getBirthdateAsLocalDate() {
    return getLocalDate(this.birthdate);
  }
}
