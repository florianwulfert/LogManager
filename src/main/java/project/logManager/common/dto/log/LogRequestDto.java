package project.logManager.common.dto.log;

import java.util.Locale;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LogRequestDto {

  public String message;
  public String severity;
  public String user;

  public String getSeverity() {
    return this.severity.toUpperCase(Locale.ROOT);
  }
}
