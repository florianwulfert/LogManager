package project.logManager.model.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.logManager.model.dto.LogDTO;
import project.logManager.model.entity.Log;

/**
 * @author - EugenFriesen 13.02.2021
 */
@Mapper(componentModel = "spring")
public interface LogDTOMapper {

  @Mapping(target = "user", source = "user.name")
  LogDTO logToLogDTO(Log log);

  List<LogDTO> logsToLogDTOs(List<Log> logs);
}
