package project.userFeaturePortal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import project.userFeaturePortal.common.dto.log.LogDTO;
import project.userFeaturePortal.common.dto.log.LogRequestDto;
import project.userFeaturePortal.common.dto.log.LogResponseDto;
import project.userFeaturePortal.model.entity.Log;
import project.userFeaturePortal.model.mapper.LogDTOMapper;
import project.userFeaturePortal.service.model.LogService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** @author - EugenFriesen 12.02.2021 */
@RequiredArgsConstructor
@RestController
@CrossOrigin
@Tag(name = "Log")
public class LogController implements LogAPI {

  private final LogService logService;
  private final LogDTOMapper logDTOMapper;

  @Override
  public ResponseEntity<LogResponseDto> getLogs(
      String severity,
      String message,
      LocalDateTime startDateTime,
      LocalDateTime endDateTime,
      String user) {

    return ResponseEntity.status(HttpStatus.OK)
        .body(
            new LogResponseDto(
                logService.getLogs(severity, message, startDateTime, endDateTime, user), null));
  }

  @Override
  @Operation(summary = "Add manually a new Log-Entry")
  public ResponseEntity<LogResponseDto> addLog(LogRequestDto allParameters) {
    String returnMessage = logService.addLog(allParameters);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new LogResponseDto(logService.getLogs(null, null, null, null, null), returnMessage));
  }

  @Override
  public List<LogDTO> getLogsByID(Integer id) {
    Log logs = logService.searchLogsByID(id);
    List<Log> returnList = new ArrayList<>();
    returnList.add(logs);
    return logDTOMapper.logsToLogDTOs(returnList);
  }

  @Override
  public ResponseEntity<LogResponseDto> deleteLogsByID(
      Integer id,
      String severity,
      String message,
      LocalDateTime startDateTime,
      LocalDateTime endDateTime,
      String user) {
    String returnMessage = logService.deleteById(id);
    return ResponseEntity.status(HttpStatus.OK)
        .body(
            new LogResponseDto(
                logService.getLogs(severity, message, startDateTime, endDateTime, user),
                returnMessage));
  }

  @Override
  public String deleteLogsBySeverity(String severity) {
    return logService.deleteBySeverity(severity);
  }

  @Override
  public ResponseEntity<LogResponseDto> deleteAll() {
    String returnMessage = logService.deleteAll();
    return ResponseEntity.status(HttpStatus.OK)
        .body(new LogResponseDto(logService.getLogs(null, null, null, null, null), returnMessage));
  }
}
