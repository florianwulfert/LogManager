package project.userFeaturePortal.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import project.userFeaturePortal.common.dto.bmi.BmiRequestDto;
import project.userFeaturePortal.common.dto.bmi.BmiResponseDto;
import project.userFeaturePortal.service.model.BmiService;

@CrossOrigin
@AllArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@Tag(name = "Bmi")
public class BmiController implements BmiAPI{

  private final BmiService bmiService;

  @Override
  public ResponseEntity<BmiResponseDto> getBmi(BmiRequestDto parameters) {
    String returnMessage = bmiService.calculateBmiAndGetBmiMessage(
        parameters.getBirthdateAsLocalDate(), parameters.weight, parameters.height);
    return ResponseEntity.status(HttpStatus.OK).body(new BmiResponseDto(returnMessage));
  }

  @Override
  public String findUserAndCalculateBMI(String user) {
    return bmiService.findUserAndGetBMI(user);
  }
}
