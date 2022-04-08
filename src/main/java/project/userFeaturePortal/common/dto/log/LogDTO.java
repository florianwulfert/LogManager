package project.userFeaturePortal.common.dto.log;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author - EugenFriesen 12.02.2021
 */
@Data
public class LogDTO {
  Integer id;
  String severity;
  String message;
  LocalDateTime timestamp;
  String user;
}
