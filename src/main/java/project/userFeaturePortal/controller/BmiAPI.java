package project.userFeaturePortal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import project.userFeaturePortal.common.dto.bmi.BmiRequestDto;
import project.userFeaturePortal.common.dto.bmi.BmiResponseDto;

public interface BmiAPI {
    @PostMapping("/bmi")
    ResponseEntity<BmiResponseDto> getBmi(@RequestBody BmiRequestDto parameters);

    @GetMapping("/bmi/{user}")
    String findUserAndCalculateBMI(@PathVariable final String user);
}
