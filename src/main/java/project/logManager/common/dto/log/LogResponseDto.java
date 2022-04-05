package project.logManager.common.dto.log;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogResponseDto {

  List<LogDTO> result;
  String returnMessage;
}
