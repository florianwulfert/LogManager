package project.userFeaturePortal.common.dto.log;

import lombok.Builder;
import lombok.Data;

import java.util.Locale;

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
