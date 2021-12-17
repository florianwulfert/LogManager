package project.logManager.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.logManager.exception.SeverityNotFoundException;
import project.logManager.model.dto.LogDTO;
import project.logManager.model.entity.Log;
import project.logManager.model.entity.User;
import project.logManager.model.mapper.LogDTOMapper;
import project.logManager.service.model.LogService;
import project.logManager.service.model.UserService;

/**
 * @author - EugenFriesen
 * 12.02.2021
 **/
@RequiredArgsConstructor
@RestController
public class LogController {

    private final LogService logService;

    private final LogDTOMapper logDTOMapper;

    private final UserService userService;

    @GetMapping("/logs")
    public List<LogDTO> getLogs(@RequestParam(required = false) final String severity,
                                @RequestParam(required = false) final String message,
                                @RequestParam(required = false) @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss") final LocalDateTime startDateTime,
                                @RequestParam(required = false) @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss") final LocalDateTime endDateTime) {
        try {
            List<Log> logs = logService.getLogs(severity, message, startDateTime, endDateTime);
            return logDTOMapper.logsToLogDTOs(logs);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/log")
    public String addLog(@RequestParam final String severity,
                         @RequestParam final String message,
                         @RequestParam final String nameUser) {
        try {
            User user = userService.findUserByName(nameUser);
            if (user == null) {
                throw new RuntimeException(String.format("User %s nicht gefunden", nameUser));
            }
            return logService.addLog(message, severity, user);
        } catch (IllegalArgumentException e) {
            throw new SeverityNotFoundException(severity);
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }


    @GetMapping("/logs/{severity}")
    public List<LogDTO> getLogsBySeverity(@PathVariable final String severity) {
        try {
            List<Log> logs = logService.getLogsBySeverity(severity);
            return logDTOMapper.logsToLogDTOs(logs);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/logs/messages")
    public List<LogDTO> filterLogsByMessageParts(@RequestParam final String message) {
        try {
            List<Log> logs = logService.searchLogsByMessageParts(message);
            return logDTOMapper.logsToLogDTOs(logs);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/logs/date")
    public List<LogDTO> getLogsByDateRange(@RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss") final LocalDateTime startDateTime,
                                           @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss") final LocalDateTime endDateTime) {
        try {
            List<Log> logs = logService.searchLogsByDateRange(startDateTime, endDateTime);
            return logDTOMapper.logsToLogDTOs(logs);
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
            return logDTOMapper.logsToLogDTOs(returnlist);
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

    @DeleteMapping("/logs/del/severity")
    public String deleteBySeverity (@RequestParam final String severity) {
        try {
            return logService.deleteBySeverity(severity);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
