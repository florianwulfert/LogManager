package project.userFeaturePortal.model.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import project.userFeaturePortal.common.message.ErrorMessages;
import project.userFeaturePortal.common.message.InfoMessages;

/**
 * @author - Florian Wulfert 25.11.2021
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user")
public class User {

  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, nullable = false)
  Integer id;

  @Column(name = "name", unique = true, nullable = false)
  String name;

  @Column(name = "birthdate", nullable = false)
  LocalDate birthdate;

  @Column(name = "weight", nullable = false)
  double weight;

  @Column(name = "height", nullable = false)
  double height;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "book")
  Book favouriteBook;

  @Column(name = "bmi")
  double bmi;

  private static final Logger LOGGER = LogManager.getLogger(User.class);

  public Integer getAgeFromBirthDate(LocalDate birthdate) {
    return LocalDate.now().getYear() - birthdate.getYear();
  }

  public Double calculateBMI() {
    BigDecimal bigDecimal =
        new BigDecimal(weight / (height * height)).setScale(2, RoundingMode.DOWN);
    return bigDecimal.doubleValue();
  }

  public String getBmiMessage() {
    int ageUser = getAgeFromBirthDate(birthdate);
    if (ageUser < 18) {
      LOGGER.warn(ErrorMessages.USER_TOO_YOUNG);
      return ErrorMessages.USER_TOO_YOUNG;
    }
    String bmiMessage = InfoMessages.BMI_MESSAGE;

    if (bmi > 18.5 && bmi <= 25) {
      return String.format(bmiMessage + InfoMessages.NORMAL_WEIGHT, bmi);
    } else if (bmi <= 18.5 && bmi > 0) {
      return String.format(bmiMessage + InfoMessages.UNDERWEIGHT, bmi);
    } else if (bmi > 25) {
      return String.format(bmiMessage + InfoMessages.OVERWEIGHT, bmi);
    } else {
      LOGGER.error(ErrorMessages.COULD_NOT_CALCULATE);
      throw new IllegalStateException(ErrorMessages.COULD_NOT_CALCULATE);
    }
  }
}
