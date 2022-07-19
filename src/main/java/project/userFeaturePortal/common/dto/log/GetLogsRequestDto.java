package project.userFeaturePortal.common.dto.log;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GetLogsRequestDto {
    Integer id;
    String severity;
    String message;
    LocalDateTime startDateTime;
    LocalDateTime endDateTime;
    String user;

}
