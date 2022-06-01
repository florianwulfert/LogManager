package project.userFeaturePortal.common.dto.log;

import lombok.Builder;
import lombok.Data;

import java.util.Locale;

@Builder
@Data
public class AddLogRequestDto {
    public String message;
    public String severity;

    public String getSeverity() {
        return this.severity.toUpperCase(Locale.ROOT);
    }
}
