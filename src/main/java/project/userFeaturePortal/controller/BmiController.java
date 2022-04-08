package project.userFeaturePortal.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.userFeaturePortal.common.dto.bmi.BmiRequestDto;
import project.userFeaturePortal.common.dto.bmi.BmiResponseDto;
import project.userFeaturePortal.service.model.BmiService;

@CrossOrigin
@AllArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@Tag(name = "Bmi")
public class BmiController {

  private final BmiService bmiService;

  @PostMapping("/bmi")
  public BmiResponseDto getBmi(@RequestBody BmiRequestDto parameters) {
    String returnMessage = bmiService.calculateBmiAndGetBmiMessage(
        parameters.getBirthdateAsLocalDate(), parameters.weight, parameters.height);
    return new BmiResponseDto(returnMessage);
  }

  @GetMapping("/bmi/{user}")
  public String findUserAndCalculateBMI(@PathVariable final String user) {
    return bmiService.findUserAndGetBMI(user);
  }
}
