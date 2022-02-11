package project.logManager.model.mapper;

import org.mapstruct.Mapper;
import project.logManager.model.dto.LogDTO;
import project.logManager.model.entity.Log;

import java.util.List;

/** @author - EugenFriesen 13.02.2021 */
@Mapper(componentModel = "spring")
public interface LogDTOMapper {
  LogDTO logToLogDTO(Log log);

  List<LogDTO> logsToLogDTOs(List<Log> logs);
}
