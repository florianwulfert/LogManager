package project.logManager.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/** @author - EugenFriesen 12.02.2021 */
@Data
public class LogDTO {
  String severity;
  String message;
  LocalDateTime timestamp;
  String user;
}
