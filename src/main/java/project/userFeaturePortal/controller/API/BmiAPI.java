package project.userFeaturePortal.controller.API;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import project.userFeaturePortal.common.dto.bmi.BmiRequestDto;
import project.userFeaturePortal.common.dto.bmi.BmiResponseDto;

public interface BmiAPI {
    @PostMapping("/bmi")
    @Operation(
            summary = "Get the BMI of an user",
            responses = {
                    @ApiResponse(
                            description = "Get BMI succeeded",
                            responseCode = "200",
                            content =
                                    @Content(
                                            mediaType = "application/json",
                                            schema =
                                                    @Schema(example =
                                                            "{\"resultMessage\":\"User has a BMI of 28 and therewith he has overweight.\"}",
                                                            allOf = BmiResponseDto.class))),
                    @ApiResponse(
                            description = "Illegal format for birthdate!",
                            responseCode = "400",
                            content =
                                    @Content(
                                            mediaType = "text/plain",
                                            schema =
                                                    @Schema(example = "Illegal format for birthdate! Use format: YYYY-MM-DD")))
            }
    )
    ResponseEntity<BmiResponseDto> getBmi(@RequestBody BmiRequestDto parameters);

    @GetMapping("/bmi/{user}")
    @Operation(
            summary = "Find user and calculate his BMI",
            responses = {
                    @ApiResponse(
                            description = "Find user and calculate his BMI succeeded.",
                            responseCode = "200",
                            content =
                            @Content(
                                    mediaType = "text/plain",
                                    schema =
                                    @Schema(example =
                                            "User has a BMI of 28 and therewith he has overweight."))),
                    @ApiResponse(
                            description = "User not found",
                            responseCode = "404",
                            content =
                            @Content(
                                    mediaType = "text/plain",
                                    schema =
                                    @Schema(example = "User named Florian not found!")))
            })
    String findUserAndCalculateBMI(@PathVariable final String user);
}
