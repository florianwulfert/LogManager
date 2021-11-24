package project.logManager.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import project.logManager.exception.SeverityNotFoundException;
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

    private final LogService logService;
    private final LogDTOMapper logDTOMapper;

    @GetMapping("/logs")
    public List<LogDTO> getLogs(@RequestParam(required = false) final String severity,
                                @RequestParam(required = false) final String message,
                                @RequestParam(required = false) @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss") final LocalDateTime startDateTime,
                                @RequestParam(required = false) @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss") final LocalDateTime endDateTime) {
        try {
            List<Log> logs = logService.getLogs(severity, message, startDateTime, endDateTime);
            return logDTOMapper.mapLogsToLogDTOs(logs);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/log")
    public String addLog(@RequestParam final String severity,
                         @RequestParam final String message) {
        try {
            return logService.addLog(message, severity);
        } catch (IllegalArgumentException ie) {
            throw new SeverityNotFoundException(severity);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @GetMapping("/logs/{severity}")
    public List<LogDTO> getLogsBySeverity(@PathVariable final String severity) {
        try {
            List<Log> logs = logService.getLogsBySeverity(severity);
            return logDTOMapper.mapLogsToLogDTOs(logs);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/logs/messages")
    public List<LogDTO> filterLogsByMessageParts(@RequestParam final String message) {
        try {
            List<Log> logs = logService.searchLogsByMessageParts(message);
            return logDTOMapper.mapLogsToLogDTOs(logs);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/logs/date")
    public List<LogDTO> getLogsByDateRange(@RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss") final LocalDateTime startDateTime,
                                           @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss") final LocalDateTime endDateTime) {
        try {
            List<Log> logs = logService.searchLogsByDateRange(startDateTime, endDateTime);
            return logDTOMapper.mapLogsToLogDTOs(logs);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/logs/id")
    public List<LogDTO> getLogsByID (@RequestParam final Integer id) {
        try {
            Log logs = logService.searchLogsByID(id);
            List<Log> returnlist=new ArrayList<>();
            returnlist.add(logs);
            return logDTOMapper.mapLogsToLogDTOs(returnlist);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @DeleteMapping("/logs/delete/{id}")
    public void deleteLogsByID (@PathVariable final Integer id) {
        try {
            logService.deleteById(id);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
