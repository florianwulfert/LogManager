package project.logManager.model.mapper;

import org.springframework.stereotype.Component;
import project.logManager.model.dto.LogDTO;
import project.logManager.model.entity.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author - EugenFriesen
 * 13.02.2021
 **/

@Component
public class LogDTOMapper {

    public List<LogDTO> mapLogsToLogDTOs(List<Log> logs) {
        List<LogDTO> logDTOS = new ArrayList<>();
        for (Log log : logs) {
            LogDTO logDTO = new LogDTO();
            logDTO.setMessage(log.getMessage());
            logDTO.setSeverity(log.getSeverity());
            logDTO.setTimestamp(log.getTimestamp());
            logDTOS.add(logDTO);
        }
        return logDTOS;
    }
}


