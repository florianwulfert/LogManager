package project.logManager.controller;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RestController
public class LogController {

    private final LogService logService;
    private final LogDTOMapper logDTOMapper;

    @GetMapping("/logs")
    public List<LogDTO> getLogs(@RequestParam(required = false) final String severity,
                                @RequestParam(required = false) final String message,
                                @RequestParam(required = false) @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss") final LocalDateTime startDateTime,
                                @RequestParam(required = false) @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss") final LocalDateTime endDateTime) {

        List<Log> logs = logService.getLogs(severity, message, startDateTime, endDateTime);
        return logDTOMapper.logsToLogDTOs(logs);
    }

    @PostMapping("/log")
    public String addLog(@RequestParam final String severity,
                         @RequestParam final String message,
                         @RequestParam final String nameUser) {

        return logService.addLog(message, severity, nameUser);
    }

    @GetMapping("/logs/{id}")
    public List<LogDTO> getLogsByID(@PathVariable final Integer id) {
        Log logs = logService.searchLogsByID(id);
        List<Log> returnlist = new ArrayList<>();
        returnlist.add(logs);
        return logDTOMapper.logsToLogDTOs(returnlist);
    }

    @DeleteMapping("/logs/delete/{id}")
    public String deleteLogsByID(@PathVariable final Integer id) {
        return logService.deleteById(id);
    }

    @DeleteMapping("/logs/delete/severity")
    public String deleteBySeverity(@RequestParam final String severity) {
        return logService.deleteBySeverity(severity);
    }

    @DeleteMapping("/logs/delete")
    public String deleteAll() {
        return logService.deleteAll();
    }
}
