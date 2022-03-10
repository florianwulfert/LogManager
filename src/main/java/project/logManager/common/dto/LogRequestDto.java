package project.logManager.common.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LogRequestDto {
    public String message;
    public String severity;
    public String user;
}
