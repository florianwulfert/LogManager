package project.userFeaturePortal.common.dto.log;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LogRequestDto {

  public AddLogRequestDto addLogRequest;
  public GetLogsRequestDto getLogsRequest;
  public String user;
}
