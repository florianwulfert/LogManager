package project.logManager.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
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

    @Nested
    class AddUserTests {
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

        @Test
        void testAddUserWhenNoUserExistsAndActorUnequalUser() throws Exception {
            userRepository.deleteAll();
            MvcResult result = mockMvc.perform(post("/user")
                            .param("actor", "Torsten")
                            .param("name", "Hugo")
                            .param("geburtsdatum", "05.11.1995")
                            .param("gewicht", "78")
                            .param("groesse", "1.80")
                            .param("lieblingsfarbe", "blau"))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
                    .andReturn();

            Assertions.assertEquals("User kann nicht angelegt werden, da noch keine User in der Datenbank " +
                            "angelegt sind. Erster User muss sich selbst anlegen! Hugo ungleich Torsten",
                    result.getResponse().getContentAsString());
        }

        @Test
        void testAddUserWhenNoUserExists() throws Exception {
            userRepository.deleteAll();
            MvcResult result = mockMvc.perform(post("/user")
                            .param("actor", "Petra")
                            .param("name", "Petra")
                            .param("geburtsdatum", "05.11.1995")
                            .param("gewicht", "78")
                            .param("groesse", "1.80")
                            .param("lieblingsfarbe", "blau"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            Assertions.assertEquals("User Petra wurde erstellt. Der User hat einen BMI von 24.07 " +
                    "und ist somit normalgewichtig.", result.getResponse().getContentAsString());
        }

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

            Assertions.assertEquals("Required path variable was not found or request param has wrong format! " +
                            "Failed to convert value of type 'java.lang.String' to required type 'java.time.LocalDate'; " +
                            "nested exception is org.springframework.core.convert.ConversionFailedException: " +
                            "Failed to convert from type [java.lang.String] to type " +
                            "[@org.springframework.web.bind.annotation.RequestParam @org.springframework.format." +
                            "annotation.DateTimeFormat java.time.LocalDate] for value 'hallo'; " +
                            "nested exception is java.lang.IllegalArgumentException: Parse attempt failed for value [hallo]",
                    result.getResponse().getContentAsString());
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

            Assertions.assertEquals("Required path variable was not found or request param has wrong format! " +
                            "Failed to convert value of type 'java.lang.String' to required type 'double'; " +
                            "nested exception is java.lang.NumberFormatException: For input string: \"hi\"",
                    result.getResponse().getContentAsString());
        }

        @Test
        void testWrongHeightFormat() throws Exception {
            MvcResult result = mockMvc.perform(post("/user")
                            .param("actor", "Petra")
                            .param("name", "Hugo")
                            .param("geburtsdatum", "08.03.2002")
                            .param("gewicht", "78")
                            .param("groesse", "hi")
                            .param("lieblingsfarbe", "braun"))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andReturn();

            Assertions.assertEquals("Required path variable was not found or request param has wrong format! " +
                            "Failed to convert value of type 'java.lang.String' to required type 'double'; " +
                            "nested exception is java.lang.NumberFormatException: For input string: \"hi\"",
                    result.getResponse().getContentAsString());
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

            Assertions.assertEquals("User Petra bereits vorhanden",
                    result.getResponse().getContentAsString());
        }
    }

    @Test
    void testFindUsers() throws Exception {
        MvcResult result = mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals("[{\"id\":1,\"name\":\"Petra\",\"geburtsdatum\":\"1999-12-13\"," +
                "\"gewicht\":65.0,\"groesse\":1.6,\"lieblingsfarbe\":\"Rot\",\"bmi\":25.39}" +
                ",{\"id\":2,\"name\":\"Torsten\",\"geburtsdatum\":\"1985-12-05\",\"gewicht\":61.3,\"groesse\":1.83," +
                "\"lieblingsfarbe\":\"Blau\",\"bmi\":18.3}," +
                "{\"id\":3,\"name\":\"Hans\",\"geburtsdatum\":\"1993-02-03\",\"gewicht\":75.7,\"groesse\":1.85," +
                "\"lieblingsfarbe\":\"Rot\",\"bmi\":22.11}]", result.getResponse().getContentAsString());
    }

    @Nested
    class FindUserByIdTests {
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
    }

    private static Stream<Arguments> getDeleteUserByIdArguments() {
        return Stream.of(
            Arguments.of(false, "/user/delete/1", "Hans", status().isOk(), "User mit der ID 1 wurde gelöscht!"),
            Arguments.of(false, "/user/delete/1", "Paul", status().isInternalServerError(), "User Paul konnte nicht identifiziert werden!"),
            Arguments.of(false, "/user/delete/1", null, status().isBadRequest(), "Required String parameter 'actor' is not present"),
            Arguments.of(false, "/user/delete/8", "Torsten", status().isInternalServerError(), "User mit der ID 8 konnte nicht gefunden werden"),
            Arguments.of(false, "/user/delete/2", "Torsten", status().isInternalServerError(), "Ein User kann sich nicht selbst löschen!"),
            Arguments.of(true, "/user/delete/1", "Torsten", status().isInternalServerError(),
                "User Petra kann nicht gelöscht werden, da er in einer anderen Tabelle referenziert wird!")
        );
    }

    @ParameterizedTest(name = "{4}")
    @MethodSource("getDeleteUserByIdArguments")
    void testGetDeleteUserById(Boolean userIsReferenced, String url, String actor, ResultMatcher status, String message) throws Exception {
        if (userIsReferenced) {
            logRepository.save(Log.builder().id(1).user(petra).message("Test").severity("INFO")
                .timestamp(LocalDateTime.of(2000, 12, 12, 12, 12, 12)).build());
        }
        MvcResult result = mockMvc.perform(delete(url)
                  .param("actor", actor))
              .andDo(print())
              .andExpect(status)
              .andReturn();

        Assertions.assertEquals(message, result.getResponse().getContentAsString());
    }

    @Nested
    class DeleteUserByNameTests {
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

            Assertions.assertEquals("Required path variable was not found or request param has wrong format! " +
                            "Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; " +
                            "nested exception is java.lang.NumberFormatException: For input string: \"name\"",
                    result.getResponse().getContentAsString());
        }

        @Test
        void whenUserNameIsUsedSomewhereThenReturnCouldNotDelete() throws Exception {
            logRepository.save(Log.builder().id(1).user(petra).message("Test").severity("INFO")
                    .timestamp(LocalDateTime.of(2000,12,12,12,12,12)).build());
            MvcResult result = mockMvc.perform(delete("/user/delete/name/Petra")
                            .param("actor", "Torsten"))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
                    .andReturn();

            Assertions.assertEquals("User Petra kann nicht gelöscht werden, da er in einer anderen Tabelle " +
                    "referenziert wird!", result.getResponse().getContentAsString());
        }
    }

    @Nested
    class DeleteAllTests {
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
            logRepository.save(Log.builder().id(1).user(petra).message("Test").severity("INFO")
                    .timestamp(LocalDateTime.of(2000,12,12,12,12,12)).build());
            MvcResult result = mockMvc.perform(delete("/user/delete"))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
                    .andReturn();

            Assertions.assertEquals("User können nicht gelöscht werden, da sie in einer anderen Tabelle " +
                    "referenziert werden", result.getResponse().getContentAsString());
        }
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