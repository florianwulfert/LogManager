package project.userFeaturePortal.common.dto.user;

import lombok.*;

import java.time.LocalDate;

@Builder
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
