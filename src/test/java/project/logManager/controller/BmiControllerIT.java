package project.logManager.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import project.logManager.model.entity.User;
import project.logManager.model.repository.UserRepository;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BmiController.class)
@AutoConfigureDataJpa
@ComponentScan(basePackages = {
    "project.logManager"
})
@Transactional
class BmiControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Test
  void whenGetBmiThenReturnBmiMessage_NormalWeighted() throws Exception {
    MvcResult result = mockMvc.perform(get("/bmi")
                .param("birthdate", "01.01.2003")
                .param("weight", "75.7")
                .param("height", "1.85"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

    Assertions.assertEquals("User has a BMI of 22.11 and therewith he has normal weight.", result.getResponse().getContentAsString());
  }

  @Test
  void whenGetBmiThenReturnBmiMessage_Overweight() throws Exception {
    MvcResult result = mockMvc.perform(get("/bmi")
                .param("birthdate", "01.01.1987")
                .param("weight", "95.2")
                .param("height", "1.82"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

    Assertions.assertEquals("User has a BMI of 28.74 and therewith he has overweight.", result.getResponse().getContentAsString());
  }

  @Test
  void whenGetBmiThenReturnBmiMessage_Underweight() throws Exception {
    MvcResult result = mockMvc.perform(get("/bmi")
                .param("birthdate", "01.01.2003")
                .param("weight", "61.3")
                .param("height", "1.83"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

    Assertions.assertEquals("User has a BMI of 18.3 and therewith he has underweight.", result.getResponse().getContentAsString());
  }

  @Test
  void whenGetBmiParamsEqualZeroThenCatchRuntimeException() throws Exception {
    MvcResult result = mockMvc.perform(get("/bmi")
                .param("birthdate", "01.01.1987")
                .param("weight", "0")
                .param("height", "0"))
        .andDo(print())
        .andExpect(status().isInternalServerError())
        .andReturn();

    Assertions.assertEquals("Infinite or NaN", result.getResponse().getContentAsString());
  }

  @Test
  void whenGetBmiWrongParamsThenThrowRuntimeException() throws Exception {
    MvcResult result = mockMvc.perform(get("/bmi")
                .param("birthdate", "01.01.1987")
                .param("weight", "-1")
                .param("height", "-1"))
        .andDo(print())
        .andExpect(status().isInternalServerError())
        .andReturn();

    Assertions.assertEquals("BMI konnte nicht berechnet werden", result.getResponse().getContentAsString());
  }

  @Test
  void whenGetBmiIsMissingParameterReturnBadRequest() throws Exception {
    MvcResult result = mockMvc.perform(get("/bmi")
            .param("birthdate", "01.01.2000")
            .param("weight", "75.0"))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn();

    Assertions.assertEquals("Required double parameter 'height' is not present",
            result.getResponse().getContentAsString());
  }

  @Test
  void whenFindUserAndCalculateBMIThenReturnBmiMessage_Underweight() throws Exception {
    createUser("underweight");

    MvcResult result = mockMvc.perform(get("/bmi/Torsten"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

    Assertions.assertEquals("Der User hat einen BMI von 18.3 und ist somit unterweightig.", result.getResponse().getContentAsString());
  }

  @Test
  void whenFindUserAndCalculateBMIThenReturnBmiMessage_Overweight() throws Exception {
    createUser("overweight");

    MvcResult result = mockMvc.perform(get("/bmi/Peter"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

    Assertions.assertEquals("Der User hat einen BMI von 28.74 und ist somit überweightig.", result.getResponse().getContentAsString());
  }

  @Test
  void whenFindUserAndCalculateBMIThenReturnBmiMessage_NormalWeight() throws Exception {
    createUser("normalWeight");

    MvcResult result = mockMvc.perform(get("/bmi/Hans"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

    Assertions.assertEquals("Der User hat einen BMI von 22.11 und ist somit normalweightig.", result.getResponse().getContentAsString());
  }

  @Test
  void whenFindUserAndCalculateBMIThenThrowException() throws Exception {
    MvcResult result = mockMvc.perform(get("/bmi/Paul"))
            .andDo(print())
            .andExpect(status().isInternalServerError())
            .andReturn();

    Assertions.assertEquals("User Paul not identified!", result.getResponse().getContentAsString());
  }

  private void createUser(String bewertung) {
    switch (bewertung) {
      case "underweight" :
        User torsten = User
            .builder()
            .name("Torsten")
            .bmi(18.3)
            .birthdate(LocalDate.of(1985,12,5))
            .weight(61.3)
            .height(1.83)
            .id(1)
            .favouriteColor("Blue")
            .build();
        userRepository.save(torsten);
        break;
      case "overweight" :
        User peter = User
            .builder()
            .name("Peter")
            .birthdate(LocalDate.of(1975,5,30))
            .bmi(28.74)
            .weight(95.2)
            .height(1.82)
            .id(2)
            .favouriteColor("Yellow")
            .build();
        userRepository.save(peter);
        break;
      case "normalWeight" :
        User hans = User
            .builder()
            .name("Hans")
            .birthdate(LocalDate.of(1993,2,3))
            .bmi(22.11)
            .weight(75.7)
            .height(1.85)
            .id(3)
            .favouriteColor("Red")
            .build();
        userRepository.save(hans);
        break;
      default: break;
    }
  }
}