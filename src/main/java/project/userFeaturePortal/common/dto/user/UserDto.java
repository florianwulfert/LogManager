package project.userFeaturePortal.common.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  Integer id;
  String name;
  LocalDate birthdate;
  double weight;
  double height;
  String favouriteBookTitel;
  double bmi;
}
