package project.userFeaturePortal.controller.API;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.userFeaturePortal.common.dto.log.LogDTO;
import project.userFeaturePortal.common.dto.log.LogRequestDto;
import project.userFeaturePortal.common.dto.log.LogResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface LogAPI {
  @GetMapping("/logs")
  @Operation(
      summary = "Get Logs filtered by severity and/or message and/or date or nothing",
      responses = {
        @ApiResponse(
            description = "Get logs succeeded",
            responseCode = "200",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LogResponseDto.class)))
      })
  ResponseEntity<LogResponseDto> getLogs(
      @RequestParam(required = false) final String severity,
      @RequestParam(required = false) final String message,
      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
          final LocalDateTime startDateTime,
      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
          final LocalDateTime endDateTime,
      @RequestParam(required = false) final String user);

  @PostMapping("/log")
  @Operation(summary = "Add manually a new Log-Entry")
  ResponseEntity<LogResponseDto> addLog(@RequestBody LogRequestDto allParameters);

  @GetMapping("/logs/{id}")
  @Operation(summary = "Found logs by id of the log")
  List<LogDTO> getLogsByID(@PathVariable final Integer id);

  @DeleteMapping("/log/id/{id}")
  @Operation(summary = "Delete logs by id of the log")
  ResponseEntity<LogResponseDto> deleteLogsByID(
      @PathVariable final Integer id,
      @RequestParam(required = false) final String severity,
      @RequestParam(required = false) final String message,
      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
          final LocalDateTime startDateTime,
      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
          final LocalDateTime endDateTime,
      @RequestParam(required = false) final String user);

  @DeleteMapping("/log/severity")
  @Operation(summary = "Delete logs by severity of the log")
  String deleteLogsBySeverity(@RequestParam final String severity);

  @DeleteMapping("/logs")
  @Operation(summary = "Delete all Logs")
  ResponseEntity<LogResponseDto> deleteAll();
}
