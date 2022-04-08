package project.logManager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import project.logManager.common.dto.LogRequestDto;
import project.logManager.common.dto.LogResponseDto;
import project.logManager.model.dto.LogDTO;
import project.logManager.model.entity.Log;
import project.logManager.model.mapper.LogDTOMapper;
import project.logManager.service.model.LogService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** @author - EugenFriesen 12.02.2021 */
@RequiredArgsConstructor
@RestController
@CrossOrigin
@Tag(name="Log")
public class LogController {

  private final LogService logService;
  private final LogDTOMapper logDTOMapper;

  @GetMapping("/logs")
  @Operation(summary="Get Logs filtered by severity and/or message and/or date or nothing", responses = {
      @ApiResponse(
          description = "Get logs succeeded",
          responseCode = "200",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = LogResponseDto.class)
          )
      )
  })
  public LogResponseDto getLogs(
      @RequestParam(required = false) final String severity,
      @RequestParam(required = false) final String message,
      @RequestParam(required = false) @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
          final LocalDateTime startDateTime,
      @RequestParam(required = false) @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
          final LocalDateTime endDateTime) {

    return new LogResponseDto(logService.getLogs(severity, message, startDateTime, endDateTime), null);
  }

  @PostMapping("/log")
  @Operation(summary="Add manually a new Log-Entry")
  public LogResponseDto addLog(@RequestBody LogRequestDto allParameters) {
    String returnMessage = logService.addLog(allParameters);
    return new LogResponseDto(logService.getLogs(null, null, null, null), returnMessage);
  }

  @GetMapping("/logs/{id}")
  @Operation(summary="Found logs by id of the log")
  public List<LogDTO> getLogsByID(@PathVariable final Integer id) {
    Log logs = logService.searchLogsByID(id);
    List<Log> returnList = new ArrayList<>();
    returnList.add(logs);
    return logDTOMapper.logsToLogDTOs(returnList);
  }

  @DeleteMapping("/logs/delete/{id}")
  @Operation(summary="Delete logs by id of the log")
  public String deleteLogsByID(@PathVariable final Integer id) {
    return logService.deleteById(id);
  }

  @DeleteMapping("/logs/delete/severity")
  @Operation(summary="Delete logs by severity of the log")
  public String deleteLogsBySeverity(@RequestParam final String severity) {
    return logService.deleteBySeverity(severity);
  }

  @DeleteMapping("/logs/delete")
  @Operation(summary="Delete all Logs")
  public String deleteAll() {
    return logService.deleteAll();
  }
}
