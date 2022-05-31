package project.userFeaturePortal.common.dto.log;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Locale;

@Builder
@Data
public class LogRequestDto {

  public String message;
  public String severity;
  public String user;
  @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
  public LocalDateTime startDateTime;
  @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
  public LocalDateTime endDateTime;

  public String getSeverity() {
    return this.severity.toUpperCase(Locale.ROOT);
  }
}
