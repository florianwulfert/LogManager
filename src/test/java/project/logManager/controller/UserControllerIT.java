package project.logManager.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
@TestPropertySource("/application-user-test.properties")
@TestInstance(Lifecycle.PER_CLASS)
class UserControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private LogRepository logRepository;

  List<User> userList = new ArrayList<>();

  @BeforeAll
  public void setup() {
    userList = createUser();
  }

  private static Stream<Arguments> getAddUserArguments() {
    return Stream.of(
        Arguments.of("User erstellt", false, "Petra", "Hugo", "05.11.1995", "78", "1.80", "blau",
            status().isOk(),
            "User Hugo wurde erstellt. Der User hat einen BMI von 24.07 und ist somit normalgewichtig."),
        Arguments.of("Erster User muss sich selbst anlegen", true, "Torsten", "Hugo", "05.11.1995", "78", "1.80", "blau",
            status().isInternalServerError(),
            "User kann nicht angelegt werden, da noch keine User in der Datenbank " +
                "angelegt sind. Erster User muss sich selbst anlegen! Hugo ungleich Torsten"),
        Arguments.of("Erster User hat sich selbst angelegt", true, "Petra", "Petra", "05.11.1995", "78", "1.80", "blau",
            status().isOk(),
            "User Petra wurde erstellt. Der User hat einen BMI von 24.07 und ist somit normalgewichtig."),
        Arguments.of("Actor nicht bekannt", false, "ActorName", "Hugo", "05.11.1995", "78", "1.80", "blau",
            status().isInternalServerError(),
            "User ActorName nicht gefunden"),
        Arguments.of("Actor nicht angegeben", false, null, "Hugo", "05.11.1995", "78", "1.80", "blau",
            status().isBadRequest(),
            "Required String parameter 'actor' is not present"),
        Arguments.of("Farbe nicht erlaubt", false, "Petra", "Hugo", "05.11.1995", "78", "1.80", "braun",
            status().isInternalServerError(),
            "Farbe falsch! Wählen Sie eine der folgenden Farben: blau, rot, orange, gelb, schwarz"),
        Arguments.of("Datum mit falschem Format angegeben", false, "Petra", "Hugo", "hallo", "78", "1.80", "blau",
            status().isBadRequest(),
            "Required path variable was not found or request param has wrong format! " +
                "Failed to convert value of type 'java.lang.String' to required type 'java.time.LocalDate'; " +
                "nested exception is org.springframework.core.convert.ConversionFailedException: " +
                "Failed to convert from type [java.lang.String] to type " +
                "[@org.springframework.web.bind.annotation.RequestParam @org.springframework.format." +
                "annotation.DateTimeFormat java.time.LocalDate] for value 'hallo'; " +
                "nested exception is java.lang.IllegalArgumentException: Parse attempt failed for value [hallo]"),
        Arguments.of("Gewicht mit falschem Format angegeben", false, "Petra", "Hugo", "05.11.1995", "hi", "1.80", "blau",
            status().isBadRequest(),
            "Required path variable was not found or request param has wrong format! " +
                "Failed to convert value of type 'java.lang.String' to required type 'double'; " +
                "nested exception is java.lang.NumberFormatException: For input string: \"hi\""),
        Arguments.of("Groesse mit falschem Format angegeben", false, "Petra", "Hugo", "05.11.1995", "78", "hi", "blau",
            status().isBadRequest(),
            "Required path variable was not found or request param has wrong format! " +
                "Failed to convert value of type 'java.lang.String' to required type 'double'; " +
                "nested exception is java.lang.NumberFormatException: For input string: \"hi\""),
        Arguments.of("Zu erzeugender User ist bereits vorhanden", false, "Petra", "Torsten", "05.11.1995", "78", "1.80", "blau",
            status().isInternalServerError(),
            "User Torsten bereits vorhanden"),
        Arguments.of("UserNameNull", false, "Petra", null, "05.11.1995", "78", "1.80", "blau",
            status().isBadRequest(), "Required String parameter 'name' is not present"),
        Arguments.of("GeburtsdatumIsNull", false, "Petra", "Albert", null, "78", "1.80", "blau",
            status().isBadRequest(), "Required LocalDate parameter 'geburtsdatum' is not present"),
        Arguments.of("GewichtIsNull", false, "Petra", "Albert", "05.11.1995", null, "1.80", "blau",
            status().isBadRequest(), "Required double parameter 'gewicht' is not present"),
        Arguments.of("GroesseIsNull", false, "Petra", "Albert", "05.11.1995", "78", null, "blau",
            status().isBadRequest(), "Required double parameter 'groesse' is not present"),
        Arguments.of("LieblingsfarbeIsNull", false, "Petra", "Albert", "05.11.1995", "78", "1.80", null,
            status().isBadRequest(), "Required String parameter 'lieblingsfarbe' is not present")
    );
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("getAddUserArguments")
  void testAddUser(String testName, Boolean isEmptyUserList, String actor, String userToAdd, String birthdate,
      String weight, String height, String lieblingsfarbe, ResultMatcher status, String message) throws Exception {
    if (isEmptyUserList) {
      userRepository.deleteAll();
    }

    MvcResult result = mockMvc.perform(post("/user")
            .param("actor", actor)
            .param("name", userToAdd)
            .param("geburtsdatum", birthdate)
            .param("gewicht", weight)
            .param("groesse", height)
            .param("lieblingsfarbe", lieblingsfarbe))
        .andDo(print())
        .andExpect(status)
        .andReturn();

    Assertions.assertEquals(message, result.getResponse().getContentAsString());
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
        Arguments.of(false, "/user/delete/8", "Torsten", status().isInternalServerError(),
            "User mit der ID 8 konnte nicht gefunden werden"),
        Arguments.of(false, "/user/delete/2", "Torsten", status().isInternalServerError(), "Ein User kann sich nicht selbst löschen!"),
        Arguments.of(true, "/user/delete/1", "Torsten", status().isInternalServerError(),
            "User Petra kann nicht gelöscht werden, da er in einer anderen Tabelle referenziert wird!")
    );
  }

  @ParameterizedTest(name = "{4}")
  @MethodSource("getDeleteUserByIdArguments")
  void testDeleteUserById(Boolean userIsReferenced, String url, String actor, ResultMatcher status, String message) throws Exception {
    if (userIsReferenced) {
      logRepository.save(Log.builder().id(1).user(userList.get(0)).message("Test").severity("INFO")
          .timestamp(LocalDateTime.of(2000, 12, 12, 12, 12, 12)).build());
    }
    MvcResult result = mockMvc.perform(delete(url)
            .param("actor", actor))
        .andDo(print())
        .andExpect(status)
        .andReturn();

    Assertions.assertEquals(message, result.getResponse().getContentAsString());
  }

  private static Stream<Arguments> getDeleteUserByNameArguments() {
    return Stream.of(
        Arguments.of("User successfully deleted by name", false, "/user/delete/name/Petra", "Torsten", status().isOk(),
            "User Petra wurde gelöscht!"),
        Arguments.of("Actor wants to delete himself", false, "/user/delete/name/Torsten", "Torsten", status().isInternalServerError(),
            "Ein User kann sich nicht selbst löschen!"),
        Arguments.of("Actor not present", false, "/user/delete/name/Petra", null, status().isBadRequest(),
            "Required String parameter 'actor' is not present"),
        Arguments.of("Actor not in database", false, "/user/delete/name/Petra", "ActorNichtBekannt", status().isInternalServerError(),
            "User mit dem Namen ActorNichtBekannt konnte nicht gefunden werden"),
        Arguments.of("User to delete not in database ", false, "/user/delete/name/UserToDeleteNichtBekannt", "Torsten",
            status().isInternalServerError(),
            "User mit dem Namen UserToDeleteNichtBekannt konnte nicht gefunden werden"),
        Arguments.of("User to delete not present", false, "/user/delete/name/", "Torsten", status().isBadRequest(),
            "Required path variable was not found or request param has wrong format! "
                + "Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; "
                + "nested exception is java.lang.NumberFormatException: For input string: \"name\""),
        Arguments.of("User is referenced in another table", true, "/user/delete/name/Petra", "Torsten", status().isInternalServerError(),
            "User Petra kann nicht gelöscht werden, da er in einer anderen Tabelle referenziert wird!")
    );
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("getDeleteUserByNameArguments")
  void testDeleteUserByName(String testname, Boolean createLog, String url, String actor, ResultMatcher status, String message)
      throws Exception {
    if (createLog) {
      logRepository.save(Log.builder().id(1).user(userList.get(0)).message("Test").severity("INFO")
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
      logRepository.save(Log.builder().id(1).user(userList.get(0)).message("Test").severity("INFO")
          .timestamp(LocalDateTime.of(2000, 12, 12, 12, 12, 12)).build());
      MvcResult result = mockMvc.perform(delete("/user/delete"))
          .andDo(print())
          .andExpect(status().isInternalServerError())
          .andReturn();

      Assertions.assertEquals("User können nicht gelöscht werden, da sie in einer anderen Tabelle " +
          "referenziert werden", result.getResponse().getContentAsString());
    }
  }

  private List<User> createUser() {
    List<User> userList = new ArrayList<>();
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
    userList.add(petra);
    userList.add(torsten);
    userList.add(hans);
    userRepository.save(petra);
    userRepository.save(torsten);
    userRepository.save(hans);
    return userList;
  }
}