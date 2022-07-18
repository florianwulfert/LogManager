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
                                    schema = @Schema(
                                            example = "{\"result\": [{\"id\":1,\"severity\":\"INFO\",\"message\":\"Test\",\"timestamp\":\"2000-12-12T12:12:12\",\"user\":\"Hans\"}]," +
                                                    "\"returnMessage\":null}",
                                            allOf = LogResponseDto.class)))
            })
    ResponseEntity<LogResponseDto> getLogs(
            @RequestParam(required = false) final String severity,
            @RequestParam(required = false) final String message,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss") final LocalDateTime startDateTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss") final LocalDateTime endDateTime,
            @RequestParam(required = false) final String user);

    @PostMapping("/log")
    @Operation(summary = "Add manually a new Log-Entry",
            responses = {
                    @ApiResponse(
                            description = "Adding a log succeeded",
                            responseCode = "201",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"result\": [{\"id\":1,\"severity\":\"INFO\",\"message\":\"Test\",\"timestamp\":\"2000-12-12T12:12:12\",\"user\":\"Hans\"}]," +
                                                    "\"returnMessage\":\"Message \"\"Test\"\" saved as INFO!\"}",
                                            allOf = LogResponseDto.class))),
                    @ApiResponse(
                            description = "Severity is false",
                            responseCode = "500",
                            content =
                            @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(example = "Severity HALLO not registered. Please choose one of the following options: TRACE, DEBUG, INFO, WARNING, ERROR, FATAL"))),
                    @ApiResponse(
                            description = "One of the parameters has wrong format.",
                            responseCode = "400",
                            content =
                            @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(example = "One of the parameters has wrong format."))),
                    @ApiResponse(
                            description = "User is not allowed to add logs",
                            responseCode = "403",
                            content =
                            @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(example = "User is not allowed to execute this operation.")))
            })
    ResponseEntity<LogResponseDto> addLog(@RequestBody LogRequestDto allParameters);

    @GetMapping("/logs/{id}")
    @Operation(summary = "Found logs by id of the log",
            responses = {
                    @ApiResponse(
                            description = "Getting a log succeeded",
                            responseCode = "200",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "[{\"id\":1,\"severity\":\"INFO\",\"message\":\"Test\",\"timestamp\":\"2000-12-12T12:12:12\",\"user\":\"Florian\"}]",
                                            allOf = LogResponseDto.class)))
            })
    List<LogDTO> getLogsByID(@PathVariable final Integer id);

    @DeleteMapping("/log/id/{id}")
    @Operation(summary = "Delete logs by id of the log",
            responses = {
                    @ApiResponse(
                            description = "Deleting a log succeeded",
                            responseCode = "200",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"result\": [{\"id\":1,\"severity\":\"INFO\",\"message\":\"Test\",\"timestamp\":\"2000-12-12T12:12:12\",\"user\":\"Hans\"}]," +
                                                    "\"returnMessage\":\"Entry with the ID 32 was deleted from database.\"}",
                                            allOf = LogResponseDto.class))),
                    @ApiResponse(
                            description = "ID does not exist",
                            responseCode = "500",
                            content =
                            @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(example = "No class project.userFeaturePortal.model.entity.Log entity with id 1 exists!"))),
            })
    ResponseEntity<LogResponseDto> deleteLogsByID(
            @PathVariable final Integer id,
            @RequestParam(required = false) final String severity,
            @RequestParam(required = false) final String message,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss") final LocalDateTime startDateTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss") final LocalDateTime endDateTime,
            @RequestParam(required = false) final String user);

    @DeleteMapping("/log/severity")
    @Operation(summary = "Delete logs by severity of the log",
            responses = {
                    @ApiResponse(
                            description = "Deleting a log succeeded",
                            responseCode = "200",
                            content =
                            @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(
                                            example = "Entries with the ID(s) 33 were deleted from database.",
                                            allOf = String.class)))
            })
    String deleteLogsBySeverity(@RequestParam final String severity);

    @DeleteMapping("/logs")
    @Operation(summary = "Delete all Logs",
            responses = {
                    @ApiResponse(
                            description = "Deleting of all logs succeeded",
                            responseCode = "200",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\n" +
                                                    "  \"result\": [],\n" +
                                                    "  \"returnMessage\": \"All logs were deleted from database!\"\n" +
                                                    "}",
                                            allOf = LogResponseDto.class)))
            })
    ResponseEntity<LogResponseDto> deleteAll();
}
