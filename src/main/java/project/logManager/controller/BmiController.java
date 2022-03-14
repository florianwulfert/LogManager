package project.logManager.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.logManager.common.dto.BmiRequestDto;
import project.logManager.service.model.BmiService;

@CrossOrigin
@AllArgsConstructor(onConstructor_ = {@Autowired})
@RestController
public class BmiController {

  private final BmiService bmiService;

  @GetMapping("/bmi")
  public String getBmi(@RequestBody BmiRequestDto parameters) {
    return bmiService.calculateBmiAndGetBmiMessage(parameters.getBirthdateAsLocalDate(), parameters.weight, parameters.height);
  }

  @GetMapping("/bmi/{user}")
  public String findUserAndCalculateBMI(@PathVariable final String user) {
    return bmiService.findUserAndGetBMI(user);
  }
}
