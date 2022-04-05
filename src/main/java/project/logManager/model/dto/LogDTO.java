package project.logManager.model.dto;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author - EugenFriesen 12.02.2021
 */
@Data
public class LogDTO {

  String severity;
  String message;
  LocalDateTime timestamp;
  String user;
}
