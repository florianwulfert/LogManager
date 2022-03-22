package project.logManager.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;
import project.logManager.common.message.ErrorMessages;
import project.logManager.common.message.InfoMessages;
import project.logManager.model.entity.User;
import project.logManager.model.repository.UserRepository;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BmiController.class)
@AutoConfigureDataJpa
@ComponentScan(basePackages = {"project.logManager"})
@Transactional
@TestPropertySource("/application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BmiControllerIT {

  @Autowired private MockMvc mockMvc;

  @Autowired private UserRepository userRepository;

  private static Stream<Arguments> getBmiArguments() {
    return Stream.of(
        Arguments.of(
            "{\"birthdate\":\"2003-01-01\",\"weight\":\"75.7\",\"height\":\"1.85\"}",
            status().isOk(),
            String.format(InfoMessages.BMI_MESSAGE, 22.11) + InfoMessages.NORMAL_WEIGHT),
        Arguments.of(
            "{\"birthdate\":\"2003-01-01\",\"weight\":\"100.5\",\"height\":\"1.85\"}",
            status().isOk(),
            String.format(InfoMessages.BMI_MESSAGE, 29.36) + InfoMessages.OVERWEIGHT),
        Arguments.of(
            "{\"birthdate\":\"2000-01-01\",\"weight\":\"45.1\",\"height\":\"1.85\"}",
            status().isOk(),
            String.format(InfoMessages.BMI_MESSAGE, 13.17) + InfoMessages.UNDERWEIGHT),
        Arguments.of(
            "{\"birthdate\":\"2003-01-01\",\"weight\":\"0\",\"height\":\"0\"}",
            status().isInternalServerError(),
            ErrorMessages.INFINITE_OR_NAN),
        Arguments.of(
            "{\"birthdate\":\"2003-01-01\",\"weight\":\"-1\",\"height\":\"1\"}",
            status().isInternalServerError(),
            ErrorMessages.COULD_NOT_CALCULATE),
        Arguments.of(
            "{\"birthdate\":\"2003-01-01\",\"weight\":\"75.7\"}",
            status().isBadRequest(),
            ErrorMessages.PARAMETER_IS_MISSING),
        Arguments.of(
            "{\"birthdate\":\"2003-01-01\",\"height\":\"1.85\"}",
            status().isBadRequest(),
            ErrorMessages.PARAMETER_IS_MISSING),
        Arguments.of(
            "{\"weight\":\"75.7\",\"height\":\"1.85\"}",
            status().isBadRequest(),
            ErrorMessages.PARAMETER_IS_MISSING),
        Arguments.of(
            "{\"birthdate\":\"2003-01-01\",\"weight\":\"hi\",\"height\":\"1.85\"}",
            status().isBadRequest(),
            ErrorMessages.PARAMETER_WRONG_FORMAT),
        Arguments.of(
            "{\"birthdate\":\"2009-01-01\",\"weight\":\"75.7\",\"height\":\"1.85\"}",
            status().isOk(),
            ErrorMessages.USER_TOO_YOUNG));
  }

  @ParameterizedTest
  @MethodSource("getBmiArguments")
  void testGetBmi(String testData, ResultMatcher status, String message) throws Exception {
    MvcResult result =
        mockMvc
            .perform(
                get("/bmi")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(testData)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status)
            .andReturn();

    Assertions.assertEquals(message, result.getResponse().getContentAsString());
  }

  private static Stream<Arguments> findUserAndCalculateBmiArguments() {
    return Stream.of(
        Arguments.of(
            "underweight",
            "/bmi/Torsten",
            status().isOk(),
            String.format(InfoMessages.BMI_MESSAGE, 18.3) + InfoMessages.UNDERWEIGHT),
        Arguments.of(
            "overweight",
            "/bmi/Peter",
            status().isOk(),
            String.format(InfoMessages.BMI_MESSAGE, 28.74) + InfoMessages.OVERWEIGHT),
        Arguments.of(
            "normalWeight",
            "/bmi/Hans",
            status().isOk(),
            String.format(InfoMessages.BMI_MESSAGE, 22.11) + InfoMessages.NORMAL_WEIGHT),
        Arguments.of(
            "userNotIdentified",
            "/bmi/ActorNichtVorhanden",
            status().isInternalServerError(),
            String.format(ErrorMessages.USER_NOT_IDENTIFIED, "ActorNichtVorhanden")));
  }

  @ParameterizedTest(name = "{3}")
  @MethodSource("findUserAndCalculateBmiArguments")
  void testFindUserAndCalculateBmi(
      String weightStatus, String url, ResultMatcher status, String message) throws Exception {
    createUser(weightStatus);
    MvcResult result = mockMvc.perform(get(url)).andDo(print()).andExpect(status).andReturn();

    Assertions.assertEquals(message, result.getResponse().getContentAsString());
  }

  private void createUser(String weightStatus) {
    switch (weightStatus) {
      case "underweight":
        User torsten =
            User.builder()
                .name("Torsten")
                .birthdate(LocalDate.of(1985, 12, 5))
                .bmi(18.3)
                .weight(61.3)
                .height(1.83)
                .id(1)
                .favouriteColor("Blue")
                .build();
        userRepository.save(torsten);
        break;
      case "overweight":
        User peter =
            User.builder()
                .name("Peter")
                .birthdate(LocalDate.of(1975, 5, 30))
                .bmi(28.74)
                .weight(95.2)
                .height(1.82)
                .id(2)
                .favouriteColor("Yellow")
                .build();
        userRepository.save(peter);
        break;
      case "normalWeight":
        User hans =
            User.builder()
                .name("Hans")
                .birthdate(LocalDate.of(1993, 2, 3))
                .bmi(22.11)
                .weight(75.7)
                .height(1.85)
                .id(3)
                .favouriteColor("Red")
                .build();
        userRepository.save(hans);
        break;
      default:
        break;
    }
  }
}
