package project.logManager.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.logManager.common.dto.bmi.BmiRequestDto;
import project.logManager.common.dto.bmi.BmiResponseDto;
import project.logManager.service.model.BmiService;

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
