package project.logManager.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import project.logManager.model.dto.LogDTO;
import project.logManager.model.entity.Log;
import project.logManager.model.mapper.LogDTOMapper;
import project.logManager.service.model.LogService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author - EugenFriesen
 * 12.02.2021
 **/

@AllArgsConstructor(onConstructor_ = {@Autowired})
@RestController
public class LogController {

    private static final Logger LOGGER = LogManager.getLogger(LogController.class);

    private final LogService logService;
    private final LogDTOMapper logDTOMapper;

    @GetMapping("/logs")
    public List<LogDTO> getLogs(@RequestParam (required = false) final String severity,
                                @RequestParam (required = false) final String message,
                                @RequestParam (required = false) @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss") final LocalDateTime startDateTime,
                                @RequestParam (required = false) @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss") final LocalDateTime endDateTime) {
        List<Log> logs = logService.getLogs(severity, message, startDateTime, endDateTime);
        return logDTOMapper.mapLogsToLogDTOs(logs);
    }

    @PostMapping("/log")
    public void addLog(@RequestParam final String severity,
                       @RequestParam final String message) {
        logService.addLog(message, severity);
    }

    @GetMapping("/logs/{severity}")
    public List<LogDTO> getLogsBySeverity(@PathVariable final String severity) {
        List<Log> logs = logService.getLogsBySeverity(severity);
        return logDTOMapper.mapLogsToLogDTOs(logs);
    }

    @GetMapping("/logs/messages")
    public List<LogDTO> filterLogsByMessageParts(@RequestParam final String message) {
        List<Log> logs = logService.searchLogsByMessageParts(message);
        return logDTOMapper.mapLogsToLogDTOs(logs);
    }

    @GetMapping("/logs/date")
    public List<LogDTO> getLogsByDateRange(@RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss") final LocalDateTime startDateTime,
                                           @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss") final LocalDateTime endDateTime) {
        List<Log> logs = logService.searchLogsByDateRange(startDateTime, endDateTime);
        return logDTOMapper.mapLogsToLogDTOs(logs);
    }

}
