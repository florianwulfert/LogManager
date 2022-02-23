package project.logManager.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.model.entity.User;
import project.logManager.service.model.BmiService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class BmiControllerTest {

  @InjectMocks BmiController systemUnderTest;

  @Mock BmiService bmiService;

  @Test
  void testGetBmi() {
    systemUnderTest.getBmi(LocalDate.of(2000, 12, 12), 90.0, 1.8);
    Mockito.verify(bmiService).calculateBmiAndGetBmiMessage(LocalDate.of(2000, 12, 12), 90.0, 1.8);
  }

  @Test
  void testFindUserAndCalculateBMI() {
    List<User> users = addTestUser();
    systemUnderTest.findUserAndCalculateBMI(users.get(0).getName());
    Mockito.verify(bmiService).findUserAndGetBMI(users.get(0).getName());
  }

  private List<User> addTestUser() {
    List<User> users = new ArrayList<>();
    users.add(
        User.builder()
            .id(1)
            .name("Peter")
            .birthdate(LocalDate.of(1988, 12, 12))
            .weight(90.0)
            .height(1.85)
            .favouriteColor("yellow")
            .bmi(26.29)
            .build());

    users.add(
        User.builder()
            .id(2)
            .name("Florian")
            .birthdate(LocalDate.of(1988, 12, 12))
            .weight(70.0)
            .height(1.85)
            .favouriteColor("yellow")
            .bmi(20.45)
            .build());
    return users;
  }
}
