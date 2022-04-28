package project.userFeaturePortal.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum UserColorEnum {
  BLUE("blue"),
  RED("red"),
  ORANGE("orange"),
  YELLOW("yellow"),
  BLACK("black");

  @Getter
  public final String color;
}
