package project.userFeaturePortal.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.userFeaturePortal.common.dto.log.LogDTO;
import project.userFeaturePortal.model.entity.Log;

import java.util.List;

/**
 * @author - EugenFriesen 13.02.2021
 */
@Mapper(componentModel = "spring")
public interface LogDTOMapper {

  @Mapping(target = "user", source = "user.name")
  LogDTO logToLogDTO(Log log);

  List<LogDTO> logsToLogDTOs(List<Log> logs);
}
