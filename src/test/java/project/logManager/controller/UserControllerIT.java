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
import project.logManager.model.entity.User;
import project.logManager.model.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureDataJpa
@ComponentScan(basePackages = {"project.logManager"})
@Transactional
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void whenActorForDeleteByIdIsNullThenReturnBadRequest() throws Exception {
        MvcResult result = mockMvc.perform(delete("/user/delete/1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        Assertions.assertEquals("Required String parameter 'actor' is not present",
                result.getResponse().getContentAsString());
    }

    @Test
    void whenIdForDeleteIsFalseThenThrowRuntimeException() throws Exception {
        createUser("Torsten");
        MvcResult result = mockMvc.perform(delete("/user/delete/8")
                        .param("actor", "Torsten"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn();

        Assertions.assertEquals("User mit der ID 8 konnte nicht gefunden werden",
                result.getResponse().getContentAsString());
    }

    @Test
    void testDeleteUserByName() throws Exception {
        createUser("Torsten");
        createUser("Petra");
        MvcResult result = mockMvc.perform(delete("/user/delete/name/Petra")
                    .param("actor", "Torsten"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals("User Petra wurde gelöscht!",
                result.getResponse().getContentAsString());
    }

    @Test
    void whenActorForDeleteByNameIsNullThenReturnBadRequest() throws Exception {
        createUser("Petra");
        MvcResult result = mockMvc.perform(delete("/user/delete/name/Petra"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        Assertions.assertEquals("Required String parameter 'actor' is not present",
                result.getResponse().getContentAsString());
    }

    @Test
    void whenUserNameToDeleteIsFalseThenThrowRuntimeException() throws Exception {
        createUser("Torsten");
        MvcResult result = mockMvc.perform(delete("/user/delete/name/Petra")
                        .param("actor", "Torsten"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn();

        Assertions.assertEquals("User mit dem Namen Petra konnte nicht gefunden werden",
                result.getResponse().getContentAsString());
    }

    @Test
    void testDeleteAll() throws Exception {
        MvcResult result = mockMvc.perform(delete("/user/delete"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals("Alle User wurden aus der Datenbank gelöscht",
                result.getResponse().getContentAsString());
    }


    private void createUser(String user) {
        switch (user) {
            case "Petra":
                User petra = User
                        .builder()
                        .name("Petra")
                        .geburtsdatum(LocalDate.of(1999, 12, 13))
                        .bmi(25.39)
                        .gewicht(65)
                        .groesse(1.60)
                        .id(1)
                        .lieblingsfarbe("Rot")
                        .build();
                userRepository.save(petra);
                break;
            case "Torsten":
                User torsten = User
                        .builder()
                        .name("Torsten")
                        .geburtsdatum(LocalDate.of(1985, 12, 5))
                        .bmi(18.3)
                        .gewicht(61.3)
                        .groesse(1.83)
                        .id(2)
                        .lieblingsfarbe("Blau")
                        .build();
                userRepository.save(torsten);
                break;
            case "Hans":
                User hans = User
                        .builder()
                        .name("Hans")
                        .geburtsdatum(LocalDate.of(1993, 2, 3))
                        .bmi(22.11)
                        .gewicht(75.7)
                        .groesse(1.85)
                        .id(3)
                        .lieblingsfarbe("Rot")
                        .build();
                userRepository.save(hans);
                break;
            default:
                break;
        }
    }
}