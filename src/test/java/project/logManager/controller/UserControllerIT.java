package project.logManager.controller;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;
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

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                Arguments.of("User created", false, "Petra", "Hugo", "05.11.1995", "78", "1.80", "blue",
                        status().isOk(),
                        "User Hugo was created. User has a BMI of 24.07 and therewith he has normal weight."),
                Arguments.of("First user has to create himself", true, "Torsten", "Hugo", "05.11.1995", "78", "1.80", "blue",
                        status().isInternalServerError(),
                        "User cannot be created because there are no user in the database yet. First user has to create himself! Hugo unequal Torsten"),
                Arguments.of("First user created himself", true, "Petra", "Petra", "05.11.1995", "78", "1.80", "blue",
                        status().isOk(),
                        "User Petra was created. User has a BMI of 24.07 and therewith he has normal weight."),
                Arguments.of("Actor not known", false, "ActorName", "Hugo", "05.11.1995", "78", "1.80", "blue",
                        status().isInternalServerError(),
                        "User ActorName not found."),
                Arguments.of("Actor not given", false, null, "Hugo", "05.11.1995", "78", "1.80", "blue",
                        status().isBadRequest(),
                        "Required String parameter 'actor' is not present"),
                Arguments.of("Color illegal", false, "Petra", "Hugo", "05.11.1995", "78", "1.80", "braun",
                        status().isInternalServerError(),
                        "Color illegal! Choose from the following options: blue, red, orange, yellow, black"),
                Arguments.of("Datum mit falschem Format angegeben", false, "Petra", "Hugo", "hallo", "78", "1.80", "blue",
                        status().isBadRequest(),
                        "Required path variable was not found or request param has wrong format! " +
                                "Failed to convert value of type 'java.lang.String' to required type 'java.time.LocalDate'; " +
                                "nested exception is org.springframework.core.convert.ConversionFailedException: " +
                                "Failed to convert from type [java.lang.String] to type " +
                                "[@org.springframework.web.bind.annotation.RequestParam @org.springframework.format." +
                                "annotation.DateTimeFormat java.time.LocalDate] for value 'hallo'; " +
                                "nested exception is java.lang.IllegalArgumentException: Parse attempt failed for value [hallo]"),
                Arguments.of("weight mit falschem Format angegeben", false, "Petra", "Hugo", "05.11.1995", "hi", "1.80", "blue",
                        status().isBadRequest(),
                        "Required path variable was not found or request param has wrong format! " +
                                "Failed to convert value of type 'java.lang.String' to required type 'double'; " +
                                "nested exception is java.lang.NumberFormatException: For input string: \"hi\""),
                Arguments.of("height mit falschem Format angegeben", false, "Petra", "Hugo", "05.11.1995", "78", "hi", "blue",
                        status().isBadRequest(),
                        "Required path variable was not found or request param has wrong format! " +
                                "Failed to convert value of type 'java.lang.String' to required type 'double'; " +
                                "nested exception is java.lang.NumberFormatException: For input string: \"hi\""),
                Arguments.of("User to create already exists", false, "Petra", "Torsten", "05.11.1995", "78", "1.80", "blue",
                        status().isInternalServerError(),
                        "User Torsten already exists."),
                Arguments.of("UserNameNull", false, "Petra", null, "05.11.1995", "78", "1.80", "blue",
                        status().isBadRequest(), "Required String parameter 'name' is not present"),
                Arguments.of("birthdateIsNull", false, "Petra", "Albert", null, "78", "1.80", "blue",
                        status().isBadRequest(), "Required LocalDate parameter 'birthdate' is not present"),
                Arguments.of("weightIsNull", false, "Petra", "Albert", "05.11.1995", null, "1.80", "blue",
                        status().isBadRequest(), "Required double parameter 'weight' is not present"),
                Arguments.of("heightIsNull", false, "Petra", "Albert", "05.11.1995", "78", null, "blue",
                        status().isBadRequest(), "Required double parameter 'height' is not present"),
                Arguments.of("favouriteColorIsNull", false, "Petra", "Albert", "05.11.1995", "78", "1.80", null,
                        status().isBadRequest(), "Required String parameter 'favouriteColor' is not present")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getAddUserArguments")
    void testAddUser(String testName, Boolean isEmptyUserList, String actor, String userToAdd, String birthdate,
                     String weight, String height, String favouriteColor, ResultMatcher status, String message) throws Exception {
        if (isEmptyUserList) {
            userRepository.deleteAll();
        }

        MvcResult result = mockMvc.perform(post("/user")
                        .param("actor", actor)
                        .param("name", userToAdd)
                        .param("birthdate", birthdate)
                        .param("weight", weight)
                        .param("height", height)
                        .param("favouriteColor", favouriteColor))
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

        Assertions.assertEquals("[{\"id\":1,\"name\":\"Petra\",\"birthdate\":\"1999-12-13\"," +
                "\"weight\":65.0,\"height\":1.6,\"favouriteColor\":\"Red\",\"bmi\":25.39}" +
                ",{\"id\":2,\"name\":\"Torsten\",\"birthdate\":\"1985-12-05\",\"weight\":61.3,\"height\":1.83," +
                "\"favouriteColor\":\"Blue\",\"bmi\":18.3}," +
                "{\"id\":3,\"name\":\"Hans\",\"birthdate\":\"1993-02-03\",\"weight\":75.7,\"height\":1.85," +
                "\"favouriteColor\":\"Red\",\"bmi\":22.11}]", result.getResponse().getContentAsString());
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

            Assertions.assertEquals("{\"id\":1,\"name\":\"Petra\",\"birthdate\":\"1999-12-13\"," +
                            "\"weight\":65.0,\"height\":1.6,\"favouriteColor\":\"Red\",\"bmi\":25.39}",
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
                Arguments.of(false, "/user/delete/1", "Hans", status().isOk(), "User with the ID 1 was deleted."),
                Arguments.of(false, "/user/delete/1", "Paul", status().isInternalServerError(), "User Paul not identified!"),
                Arguments.of(false, "/user/delete/1", null, status().isBadRequest(), "Required String parameter 'actor' is not present"),
                Arguments.of(false, "/user/delete/8", "Torsten", status().isInternalServerError(), "User with the ID 8 not found."),
                Arguments.of(false, "/user/delete/2", "Torsten", status().isInternalServerError(), "User cannot delete himself!"),
                Arguments.of(true, "/user/delete/1", "Torsten", status().isInternalServerError(),
                        "User Petra cannot be deleted because he is referenced in another table!")
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
                Arguments.of("User successfully deleted by name", false, "/user/delete/name/Petra", "Torsten", status().isOk(), "User named Petra was deleted."),
                Arguments.of("Actor wants to delete himself", false, "/user/delete/name/Torsten", "Torsten", status().isInternalServerError(),
                        "User cannot delete himself!"),
                Arguments.of("Actor not present", false, "/user/delete/name/Petra", null, status().isBadRequest(),
                        "Required String parameter 'actor' is not present"),
                Arguments.of("Actor not in database", false, "/user/delete/name/Petra", "ActorNichtBekannt", status().isInternalServerError(),
                        "User named ActorNichtBekannt not found!"),
                Arguments.of("User to delete not in database ", false, "/user/delete/name/UserToDeleteNichtBekannt", "Torsten", status().isInternalServerError(),
                        "User named UserToDeleteNichtBekannt not found!"),
                Arguments.of("User to delete not present", false, "/user/delete/name/", "Torsten", status().isBadRequest(),
                        "Required path variable was not found or request param has wrong format! "
                                + "Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; "
                                + "nested exception is java.lang.NumberFormatException: For input string: \"name\""),
                Arguments.of("User is referenced in another table", true, "/user/delete/name/Petra", "Torsten", status().isInternalServerError(),
                        "User Petra cannot be deleted because he is referenced in another table!")
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

            Assertions.assertEquals("All users were deleted from database!",
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

            Assertions.assertEquals("Users cannot be deleted because they are referenced in another table!", result.getResponse().getContentAsString());
        }
    }

    private List<User> createUser(String user) {
      List<User> userList = new ArrayList<>();
      User petra = User
                        .builder()
                        .id(1)
                        .name("Petra")
                        .birthdate(LocalDate.of(1999, 12, 13))
                        .bmi(25.39)
                        .weight(65)
                        .height(1.60)
                        .favouriteColor("Red")
                        .build();
                userRepository.saveAndFlush(petra);
                User torsten = User
                        .builder()
                        .name("Torsten")
                        .birthdate(LocalDate.of(1985, 12, 5))
                        .bmi(18.3)
                        .weight(61.3)
                        .height(1.83)
                        .id(2)
                        .favouriteColor("Blue")
                        .build();
                userRepository.saveAndFlush(torsten);
                User hans = User
                        .builder()
                        .name("Hans")
                        .birthdate(LocalDate.of(1993, 2, 3))
                        .bmi(22.11)
                        .weight(75.7)
                        .height(1.85)
                        .id(3)
                        .favouriteColor("Red")
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