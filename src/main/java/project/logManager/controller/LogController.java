package project.logManager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import project.logManager.exception.SeverityNotFoundException;
import project.logManager.model.dto.LogDTO;
import project.logManager.model.entity.Log;
import project.logManager.model.entity.User;
import project.logManager.model.mapper.LogDTOMapper;
import project.logManager.service.model.LogService;
import project.logManager.service.model.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
            List<User> user = userService.findUserByName(nameUser);
            if (user == null) {
                throw new RuntimeException(String.format("User %s nicht gefunden", nameUser));
            }
            return logService.addLog(message, severity, user.get(0));
        } catch (IllegalArgumentException e) {
            throw new SeverityNotFoundException(severity);
        } catch (RuntimeException e) {
            return e.getMessage();
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

    @DeleteMapping("/logs/delete/severity")
    public String deleteBySeverity (@RequestParam final String severity) {
        try {
            return logService.deleteBySeverity(severity);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
