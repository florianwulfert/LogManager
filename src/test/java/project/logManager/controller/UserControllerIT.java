package project.logManager.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import project.logManager.model.entity.Log;
import project.logManager.model.entity.User;
import project.logManager.model.repository.LogRepository;
import project.logManager.model.repository.UserRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureDataJpa
@ComponentScan(basePackages = {"project.logManager"})
@Transactional
@TestPropertySource("/application-test.properties")
@TestInstance(Lifecycle.PER_CLASS)
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LogRepository logRepository;

    User petra;
    User torsten;
    User hans;

    @BeforeAll
    public void setup() {
        petra = createUser("Petra");
        torsten = createUser("Torsten");
        hans = createUser("Hans");
    }

    @Test
    void testAddUser() throws Exception {
        MvcResult result = mockMvc.perform(post("/user")
                        .param("actor", "Petra")
                        .param("name", "Hugo")
                        .param("geburtsdatum", "05.11.1995")
                        .param("gewicht", "78")
                        .param("groesse", "1.80")
                        .param("lieblingsfarbe", "blau"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals("User Hugo wurde erstellt. Der User hat einen BMI von 24.07 " +
                "und ist somit normalgewichtig.", result.getResponse().getContentAsString());
    }

    // TODO : User kann nicht angelegt werden, da noch keine User in der Datenbank...

    @Test
    void testActorIsMissingAtAddUser() throws Exception {
        userRepository.delete(petra);
        MvcResult result = mockMvc.perform(post("/user")
                        .param("actor", "Petra")
                        .param("name", "Hugo")
                        .param("geburtsdatum", "05.11.1995")
                        .param("gewicht", "78")
                        .param("groesse", "1.80")
                        .param("lieblingsfarbe", "blau"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn();

        Assertions.assertEquals("User Petra nicht gefunden", result.getResponse().getContentAsString());
    }

    @Test
    void testParameterIsMissingAtAddUser() throws Exception {
        MvcResult result = mockMvc.perform(post("/user")
                        .param("name", "Hugo")
                        .param("geburtsdatum", "05.11.1995")
                        .param("gewicht", "78")
                        .param("groesse", "1.80")
                        .param("lieblingsfarbe", "blau"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        Assertions.assertEquals("Required String parameter 'actor' is not present",
                result.getResponse().getContentAsString());
    }

    @Test
    void testColorIsBadAtAddUser() throws Exception {
        MvcResult result = mockMvc.perform(post("/user")
                        .param("actor", "Petra")
                        .param("name", "Hugo")
                        .param("geburtsdatum", "05.11.1995")
                        .param("gewicht", "78")
                        .param("groesse", "1.80")
                        .param("lieblingsfarbe", "braun"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn();

        Assertions.assertEquals("Farbe falsch! Wählen Sie eine der folgenden Farben: blau, rot, " +
                "orange, gelb, schwarz", result.getResponse().getContentAsString());
    }

    //Required path variable was not found
    @Test
    void testWrongDateFormat() throws Exception {
        MvcResult result = mockMvc.perform(post("/user")
                        .param("actor", "Petra")
                        .param("name", "Hugo")
                        .param("geburtsdatum", "hallo")
                        .param("gewicht", "78")
                        .param("groesse", "1.80")
                        .param("lieblingsfarbe", "braun"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void testWrongWeightFormat() throws Exception {
        MvcResult result = mockMvc.perform(post("/user")
                        .param("actor", "Petra")
                        .param("name", "Hugo")
                        .param("geburtsdatum", "08.03.2002")
                        .param("gewicht", "hi")
                        .param("groesse", "1.80")
                        .param("lieblingsfarbe", "braun"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void testWrongHeightFormat() throws Exception {
        MvcResult result = mockMvc.perform(post("/user")
                        .param("actor", "Petra")
                        .param("name", "Hugo")
                        .param("geburtsdatum", "hallo")
                        .param("gewicht", "78")
                        .param("groesse", "hi")
                        .param("lieblingsfarbe", "braun"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void testUserIsAlreadyDefined() throws Exception {
        MvcResult result = mockMvc.perform(post("/user")
                        .param("actor", "Petra")
                        .param("name", "Petra")
                        .param("geburtsdatum", "05.11.1995")
                        .param("gewicht", "78")
                        .param("groesse", "1.80")
                        .param("lieblingsfarbe", "blau"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    void testFindUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void testFindUserById() throws Exception {
        MvcResult result = mockMvc.perform(get("/user/id")
                        .param("id", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals("{\"id\":1,\"name\":\"Petra\",\"geburtsdatum\":\"1999-12-13\"," +
                        "\"gewicht\":65.0,\"groesse\":1.6,\"lieblingsfarbe\":\"Rot\",\"bmi\":25.39}",
                result.getResponse().getContentAsString());
    }

    @Test
    void whenIdToFindIsNullThenReturnBadRequest() throws Exception {
        MvcResult result = mockMvc.perform(get("/user/id"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        Assertions.assertEquals("Required Integer parameter 'id' is not present",
                result.getResponse().getContentAsString());
    }

    @Test
    void whenIdToFindNotFoundThenReturnNull() throws Exception {
        MvcResult result = mockMvc.perform(get("/user/id")
                        .param("id", "50"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals("null", result.getResponse().getContentAsString());
    }

    @Test
    void testDeleteUserById() throws Exception {
        MvcResult result = mockMvc.perform(delete("/user/delete/1")
                .param("actor", "Hans"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals("User mit der ID 1 wurde gelöscht!",
                result.getResponse().getContentAsString());
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
        MvcResult result = mockMvc.perform(delete("/user/delete/8")
                        .param("actor", "Torsten"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn();

        Assertions.assertEquals("User mit der ID 8 konnte nicht gefunden werden",
                result.getResponse().getContentAsString());
    }

    @Test
    void testActorIsTryingToDeleteHimselfId() throws Exception {
        MvcResult result = mockMvc.perform(delete("/user/delete/2")
                .param("actor", "Torsten"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    void testDeleteUserByName() throws Exception {
        MvcResult result = mockMvc.perform(delete("/user/delete/name/Petra")
                        .param("actor", "Torsten"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals("User Petra wurde gelöscht!",
                result.getResponse().getContentAsString());
    }

    @Test
    void testActorIsTryingToDeleteHimselfName() throws Exception {
        MvcResult result = mockMvc.perform(delete("/user/delete/name/Torsten")
                        .param("actor", "Torsten"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn();

        Assertions.assertEquals("Ein User kann sich nicht selbst löschen!",
                result.getResponse().getContentAsString());
    }

    @Test
    void whenActorForDeleteByNameIsNullThenReturnBadRequest() throws Exception {
        MvcResult result = mockMvc.perform(delete("/user/delete/name/Petra"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        Assertions.assertEquals("Required String parameter 'actor' is not present",
                result.getResponse().getContentAsString());
    }

    //
    @Test
    void whenActorForDeleteByNameNotFoundThenReturnBadRequest() throws Exception {
        MvcResult result = mockMvc.perform(delete("/user/delete/name/Petra")
                        .param("actor", "Florian"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn();

        Assertions.assertEquals("User mit dem Namen Florian konnte nicht gefunden werden",
                result.getResponse().getContentAsString());
    }

    @Test
    void whenUserNameToDeleteNotFoundThenThrowRuntimeException() throws Exception {
        userRepository.delete(petra);
        MvcResult result = mockMvc.perform(delete("/user/delete/name/Petra")
                        .param("actor", "Torsten"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn();

        Assertions.assertEquals("User mit dem Namen Petra konnte nicht gefunden werden",
                result.getResponse().getContentAsString());
    }

    @Test
    void whenUserNameToDeleteIsNullThenThrowRuntimeException() throws Exception {
        MvcResult result = mockMvc.perform(delete("/user/delete/name/")
                        .param("actor", "Torsten"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        Assertions.assertEquals("Required path variable was not found!",
                result.getResponse().getContentAsString());
    }

    @Test
    void testDeleteAll() throws Exception {
        logRepository.deleteAll();
        MvcResult result = mockMvc.perform(delete("/user/delete"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals("Alle User wurden aus der Datenbank gelöscht",
                result.getResponse().getContentAsString());
    }

    @Test
    void whenUserIsUsedSomewhereThenReturnCouldNotDelete() throws Exception {
        User user = createUser("Petra");
        logRepository.save(Log.builder().id(1).user(user).message("Test").severity("INFO")
                .timestamp(LocalDateTime.of(2000,12,12,12,12,12)).build());
        MvcResult result = mockMvc.perform(delete("/user/delete"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn();

        Assertions.assertEquals("User können nicht gelöscht werden, da sie in einer anderen Tabelle " +
                        "referenziert werden", result.getResponse().getContentAsString());
    }


    private User createUser(String user) {
        switch (user) {
            case "Petra":
                User petra = User
                        .builder()
                        .id(1)
                        .name("Petra")
                        .geburtsdatum(LocalDate.of(1999, 12, 13))
                        .bmi(25.39)
                        .gewicht(65)
                        .groesse(1.60)
                        .lieblingsfarbe("Rot")
                        .build();
                userRepository.save(petra);
                return petra;
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
                return torsten;
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
                return hans;
            default:
                break;
        }
        return null;
    }
}