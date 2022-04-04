package project.logManager.common.dto.log;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.logManager.model.dto.LogDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogResponseDto {

  List<LogDTO> result;
  String returnMessage;
}
