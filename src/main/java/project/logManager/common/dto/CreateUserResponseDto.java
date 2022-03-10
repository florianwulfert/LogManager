package project.logManager.common.dto;

import lombok.Builder;
import lombok.Data;
import project.logManager.model.entity.User;

@Builder
@Data
public class CreateUserResponseDto {
  String message;
  User user;
  boolean success;
}
