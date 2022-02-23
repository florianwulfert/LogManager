package project.logManager.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.logManager.service.model.BmiService;

import java.time.LocalDate;

@AllArgsConstructor(onConstructor_ = {@Autowired})
@RestController
public class BmiController {

  private final BmiService bmiService;

  @GetMapping("/bmi")
  public String getBmi(
      @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate birthdate,
      @RequestParam double weight,
      @RequestParam double height) {
    return bmiService.calculateBmiAndGetBmiMessage(birthdate, weight, height);
  }

  @GetMapping("/bmi/{user}")
  public String findUserAndCalculateBMI(@PathVariable final String user) {
    return bmiService.findUserAndGetBMI(user);
  }
}
