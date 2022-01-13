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
  void whenGetBmiThenReturnBmiMessage_Normalgewichtig() throws Exception{
    MvcResult result = mockMvc.perform(get("/bmi")
                .param("geburtsdatum", "01.01.2003")
                .param("gewicht", "75.7")
                .param("groesse", "1.85"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

    Assertions.assertEquals("Der User hat einen BMI von 22.11 und ist somit normalgewichtig.", result.getResponse().getContentAsString());
  }

  @Test
  void whenGetBmiThenReturnBmiMessage_Uebergewichtig() throws Exception{
    MvcResult result = mockMvc.perform(get("/bmi")
                .param("geburtsdatum", "01.01.1987")
                .param("gewicht", "95.2")
                .param("groesse", "1.82"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

    Assertions.assertEquals("Der User hat einen BMI von 28.74 und ist somit übergewichtig.", result.getResponse().getContentAsString());
  }

  @Test
  void whenGetBmiThenReturnBmiMessage_Untergewichtig() throws Exception{
    MvcResult result = mockMvc.perform(get("/bmi")
                .param("geburtsdatum", "01.01.2003")
                .param("gewicht", "61.3")
                .param("groesse", "1.83"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

    Assertions.assertEquals("Der User hat einen BMI von 18.3 und ist somit untergewichtig.", result.getResponse().getContentAsString());
  }

  @Test
  void whenGetBmiParamsEqualZeroThenCatchRuntimeException() throws Exception{
    MvcResult result = mockMvc.perform(get("/bmi")
                .param("geburtsdatum", "01.01.1987")
                .param("gewicht", "0")
                .param("groesse", "0"))
        .andDo(print())
        .andExpect(status().isInternalServerError())
        .andReturn();

    Assertions.assertEquals("Infinite or NaN", result.getResponse().getContentAsString());
  }

  @Test
  void whenGetBmiWrongParamsThenThrowRuntimeException() throws Exception{
    MvcResult result = mockMvc.perform(get("/bmi")
                .param("geburtsdatum", "01.01.1987")
                .param("gewicht", "-1")
                .param("groesse", "-1"))
        .andDo(print())
        .andExpect(status().isInternalServerError())
        .andReturn();

    Assertions.assertEquals("BMI konnte nicht berechnet werden", result.getResponse().getContentAsString());
  }

  @Test
  void whenfindUserAndCalculateBMIThenReturnBmiMessage_Untergewichtig() throws Exception {
    createUser("untergewichtig");

    MvcResult result = mockMvc.perform(get("/bmi/Torsten"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

    Assertions.assertEquals("Der User hat einen BMI von 18.3 und ist somit untergewichtig.", result.getResponse().getContentAsString());
  }

  @Test
  void whenfindUserAndCalculateBMIThenReturnBmiMessage_Uebergewichtig() throws Exception {
    createUser("uebergewichtig");

    MvcResult result = mockMvc.perform(get("/bmi/Peter"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

    Assertions.assertEquals("Der User hat einen BMI von 28.74 und ist somit übergewichtig.", result.getResponse().getContentAsString());
  }

  @Test
  void whenfindUserAndCalculateBMIThenReturnBmiMessage_Normalgewichtig() throws Exception {
    createUser("normalgewichtig");

    MvcResult result = mockMvc.perform(get("/bmi/Hans"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

    Assertions.assertEquals("Der User hat einen BMI von 22.11 und ist somit normalgewichtig.", result.getResponse().getContentAsString());
  }

  @Test
  void whenFindUserAndCalculateBMIThenThrowException() throws Exception{
    MvcResult result = mockMvc.perform(get("/bmi/Paul"))
            .andDo(print())
            .andExpect(status().isInternalServerError())
            .andReturn();

    Assertions.assertEquals("User Paul konnte nicht identifiziert werden!", result.getResponse().getContentAsString());
  }

  private void createUser(String bewertung) {
    switch (bewertung) {
      case "untergewichtig" :
        User torsten = User
            .builder()
            .name("Torsten")
            .geburtsdatum(LocalDate.of(1985,12,5))
            .bmi(18.3)
            .gewicht(61.3)
            .groesse(1.83)
            .id(1)
            .lieblingsfarbe("Blau")
            .build();
        userRepository.save(torsten);
        break;
      case "uebergewichtig" :
        User peter = User
            .builder()
            .name("Peter")
            .geburtsdatum(LocalDate.of(1975,5,30))
            .bmi(28.74)
            .gewicht(95.2)
            .groesse(1.82)
            .id(2)
            .lieblingsfarbe("Gelb")
            .build();
        userRepository.save(peter);
        break;
      case "normalgewichtig" :
        User hans = User
            .builder()
            .name("Hans")
            .geburtsdatum(LocalDate.of(1993,2,3))
            .bmi(22.11)
            .gewicht(75.7)
            .groesse(1.85)
            .id(3)
            .lieblingsfarbe("Rot")
            .build();
        userRepository.save(hans);
        break;
      default: break;
    }
  }
}