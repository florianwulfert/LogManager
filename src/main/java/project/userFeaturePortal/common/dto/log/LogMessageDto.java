package project.userFeaturePortal.common.dto.log;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LogMessageDto {

  String message;
  String returnMessage;
}
