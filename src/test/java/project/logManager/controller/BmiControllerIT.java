package project.logManager.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

  @Test
  void whenGetBmiThenReturnBmiMessage_Normalgewichtig() throws Exception{
    MvcResult result = mockMvc.perform(
            get("/bmi")
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
    MvcResult result = mockMvc.perform(
            get("/bmi")
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
    MvcResult result = mockMvc.perform(
            get("/bmi")
                .param("geburtsdatum", "01.01.2003")
                .param("gewicht", "61.3")
                .param("groesse", "1.83"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

    Assertions.assertEquals("Der User hat einen BMI von 18.3 und ist somit untergewichtig.", result.getResponse().getContentAsString());
  }
}