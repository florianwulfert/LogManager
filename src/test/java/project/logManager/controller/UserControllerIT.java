package project.logManager.controller;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
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
import project.logManager.common.message.ErrorMessages;
import project.logManager.common.message.InfoMessages;
import project.logManager.common.message.TestMessages;
import project.logManager.model.entity.Log;
import project.logManager.model.entity.User;
import project.logManager.model.repository.LogRepository;
import project.logManager.model.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureDataJpa
@ComponentScan(basePackages = { "project.logManager" })
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
                                Arguments.of(
                                                "User created",
                                                false,
                                                "{\"actor\":\"Petra\",\"name\":\"Hugo\",\"birthdate\":\"1999-12-13\",\"weight\":78.0,\"height\":1.8,\"favouriteColor\":\"Red\"}",
                                                status().isOk(),
                                                String.format(InfoMessages.USER_CREATED + InfoMessages.BMI_MESSAGE,
                                                                "Hugo", 24.07)
                                                                + InfoMessages.NORMAL_WEIGHT),
                                Arguments.of(
                                                "First user has to create himself",
                                                true,
                                                "{\"actor\":\"Torsten\",\"name\":\"Hugo\",\"birthdate\":\"1995-11-05\",\"weight\":78.0,\"height\":1.8,\"favouriteColor\":\"blue\"}",
                                                status().isInternalServerError(),
                                                ErrorMessages.NO_USERS_YET + "Hugo unequal Torsten"),
                                Arguments.of(
                                                "First user created himself",
                                                true,
                                                "{\"actor\":\"Petra\",\"name\":\"Petra\",\"birthdate\":\"1995-11-05\",\"weight\":78.0,\"height\":1.8,\"favouriteColor\":\"blue\"}",
                                                status().isOk(),
                                                String.format(InfoMessages.USER_CREATED + InfoMessages.BMI_MESSAGE,
                                                                "Petra", 24.07)
                                                                + InfoMessages.NORMAL_WEIGHT),
                                Arguments.of(
                                                "Actor not known",
                                                false,
                                                "{\"actor\":\"UnknownActor\",\"name\":\"Hugo\",\"birthdate\":\"1995-11-05\",\"weight\":78.0,\"height\":1.8,\"favouriteColor\":\"Red\"}",
                                                status().isInternalServerError(),
                                                String.format(ErrorMessages.USER_NOT_FOUND_NAME, "UnknownActor")),
                                Arguments.of(
                                                "Actor not given",
                                                false,
                                                "{\"name\":\"Hugo\",\"birthdate\":\"1995-11-05\",\"weight\":78.0,\"height\":1.8,\"favouriteColor\":\"Red\"}",
                                                status().isBadRequest(),
                                                ErrorMessages.PARAMETER_IS_MISSING),
                                Arguments.of(
                                                "Color illegal",
                                                false,
                                                "{\"actor\":\"Torsten\",\"name\":\"Hugo\",\"birthdate\":\"1995-11-05\",\"weight\":78.0,\"height\":1.8,\"favouriteColor\":\"purple\"}",
                                                status().isInternalServerError(),
                                                ErrorMessages.COLOR_ILLEGAL_PLUS_CHOICE),
                                Arguments.of(
                                                "Date has wrong format",
                                                false,
                                                "{\"actor\":\"Torsten\",\"name\":\"Hugo\",\"birthdate\":\"hallo\",\"weight\":78.0,\"height\":1.8,\"favouriteColor\":\"blue\"}",
                                                status().isBadRequest(),
                                                ErrorMessages.ILLEGAL_BIRTHDATE_FORMAT),
                                Arguments.of(
                                                "weight has wrong format",
                                                false,
                                                "{\"actor\":\"Torsten\",\"name\":\"Hugo\",\"birthdate\":\"1995-11-05\",\"weight\":\"hi\",\"height\":1.8,\"favouriteColor\":\"blue\"}",
                                                status().isBadRequest(),
                                                ErrorMessages.PARAMETER_WRONG_FORMAT),
                                Arguments.of(
                                                "height has wrong format",
                                                false,
                                                "{\"actor\":\"Torsten\",\"name\":\"Hugo\",\"birthdate\":\"1995-11-05\",\"weight\":\"78.0\",\"height\":\"hi\",\"favouriteColor\":\"blue\"}",
                                                status().isBadRequest(),
                                                ErrorMessages.PARAMETER_WRONG_FORMAT),
                                Arguments.of(
                                                "User to create already exists",
                                                false,
                                                "{\"actor\":\"Torsten\",\"name\":\"Petra\",\"birthdate\":\"1995-11-05\",\"weight\":\"78.0\",\"height\":1.8,\"favouriteColor\":\"blue\"}",
                                                status().isInternalServerError(),
                                                String.format(ErrorMessages.USER_EXISTS, "Petra")),
                                Arguments.of(
                                                "UserNameNull",
                                                false,
                                                "{\"actor\":\"Torsten\",\"birthdate\":\"1995-11-05\",\"weight\":\"78.0\",\"height\":1.8,\"favouriteColor\":\"blue\"}",
                                                status().isBadRequest(),
                                                ErrorMessages.PARAMETER_IS_MISSING),
                                Arguments.of(
                                                "birthdateIsNull",
                                                false,
                                                "{\"actor\":\"Torsten\",\"name\":\"Hugo\",\"weight\":\"78.0\",\"height\":1.8,\"favouriteColor\":\"blue\"}",
                                                status().isBadRequest(),
                                                ErrorMessages.PARAMETER_IS_MISSING),
                                Arguments.of(
                                                "weightIsNull",
                                                false,
                                                "{\"actor\":\"Torsten\",\"name\":\"Hugo\",\"birthdate\":\"1995-11-05\",\"height\":1.8,\"favouriteColor\":\"blue\"}",
                                                status().isBadRequest(),
                                                ErrorMessages.PARAMETER_IS_MISSING),
                                Arguments.of(
                                                "heightIsNull",
                                                false,
                                                "{\"actor\":\"Torsten\",\"name\":\"Hugo\",\"birthdate\":\"1995-11-05\",\"weight\":\"78.0\",\"favouriteColor\":\"blue\"}",
                                                status().isBadRequest(),
                                                ErrorMessages.PARAMETER_IS_MISSING),
                                Arguments.of(
                                                "favouriteColorIsNull",
                                                false,
                                                "{\"actor\":\"Torsten\",\"name\":\"Hugo\",\"birthdate\":\"1995-11-05\",\"weight\":\"78.0\",\"height\":\"1.8\"}",
                                                status().isBadRequest(),
                                                ErrorMessages.PARAMETER_IS_MISSING));
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("getAddUserArguments")
        void testAddUser(
                        String testName,
                        Boolean isEmptyUserList,
                        String content,
                        ResultMatcher status,
                        String message)
                        throws Exception {
                if (isEmptyUserList) {
                        userRepository.deleteAll();
                }

                MvcResult result = mockMvc
                                .perform(
                                                post("/user")
                                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                .content(content)
                                                                .accept(MediaType.APPLICATION_JSON_VALUE))
                                .andDo(print())
                                .andExpect(status)
                                .andReturn();

                Assertions.assertEquals(message, result.getResponse().getContentAsString());
        }

        @Test
        void testFindUsers() throws Exception {
                MvcResult result = mockMvc.perform(get("/users")).andDo(print()).andExpect(status().isOk()).andReturn();

                Assertions.assertEquals(
                                TestMessages.PETRA_TORSTEN_HANS, result.getResponse().getContentAsString());
        }

        @Nested
        class FindUserByIdTests {
                @Test
                void testFindUserById() throws Exception {
                        MvcResult result = mockMvc
                                        .perform(get("/user/id").param("id", "1"))
                                        .andDo(print())
                                        .andExpect(status().isOk())
                                        .andReturn();

                        Assertions.assertEquals(TestMessages.PETRA, result.getResponse().getContentAsString());
                }

                @Test
                void whenIdToFindIsNullThenReturnBadRequest() throws Exception {
                        MvcResult result = mockMvc
                                        .perform(get("/user/id"))
                                        .andDo(print())
                                        .andExpect(status().isBadRequest())
                                        .andReturn();

                        Assertions.assertEquals(
                                        ErrorMessages.ID_NOT_PRESENT, result.getResponse().getContentAsString());
                }

                @Test
                void whenIdToFindNotFoundThenReturnNull() throws Exception {
                        MvcResult result = mockMvc
                                        .perform(get("/user/id").param("id", "50"))
                                        .andDo(print())
                                        .andExpect(status().isOk())
                                        .andReturn();

                        Assertions.assertEquals("null", result.getResponse().getContentAsString());
                }
        }


        private static Stream<Arguments> getDeleteUserByIdArguments() {
                return Stream.of(
                                Arguments.of(
                                                false,
                                                "/user/delete/1",
                                                "Hans",
                                                status().isOk(),
                                                String.format(InfoMessages.USER_DELETED_ID, 1)),
                                Arguments.of(
                                                false,
                                                "/user/delete/1",
                                                "Paul",
                                                status().isInternalServerError(),
                                                String.format(ErrorMessages.USER_NOT_IDENTIFIED, "Paul")),
                                Arguments.of(
                                                false, "/user/delete/1", null, status().isBadRequest(),
                                                TestMessages.ACTOR_NOT_PRESENT),
                                Arguments.of(
                                                false,
                                                "/user/delete/8",
                                                "Torsten",
                                                status().isInternalServerError(),
                                                String.format(ErrorMessages.USER_NOT_FOUND_ID, 8)),
                                Arguments.of(
                                                false,
                                                "/user/delete/2",
                                                "Torsten",
                                                status().isInternalServerError(),
                                                ErrorMessages.USER_DELETE_HIMSELF),
                                Arguments.of(
                                                true,
                                                "/user/delete/1",
                                                "Torsten",
                                                status().isInternalServerError(),
                                                String.format(ErrorMessages.USER_REFERENCED, "Petra")));
        }

        @ParameterizedTest(name = "{4}")
        @MethodSource("getDeleteUserByIdArguments")
        void testDeleteUserById(
                        Boolean userIsReferenced, String url, String actor, ResultMatcher status, String message)
                        throws Exception {
                if (userIsReferenced) {
                        logRepository.save(
                                        Log.builder()
                                                        .id(1)
                                                        .user(userList.get(0))
                                                        .message("Test")
                                                        .severity("INFO")
                                                        .timestamp(LocalDateTime.of(2000, 12, 12, 12, 12, 12))
                                                        .build());
                }
                MvcResult result = mockMvc
                                .perform(delete(url).param("actor", actor))
                                .andDo(print())
                                .andExpect(status)
                                .andReturn();

                Assertions.assertEquals(message, result.getResponse().getContentAsString());
        }


    
  private static Stream<Arguments> getDeleteUserByNameArguments() {
    return Stream.of(
        Arguments.of(
            "User successfully deleted by name",
            false,
            "/user/delete/name/Petra",
            "Torsten",
            status().isOk(),
            String.format(InfoMessages.USER_DELETED_NAME, "Petra")),
        Arguments.of(
            "Actor wants to delete himself",
            false,
            "/user/delete/name/Torsten",
            "Torsten",
            status().isInternalServerError(),
            ErrorMessages.USER_DELETE_HIMSELF),
        Arguments.of(
            "Actor not present",
            false,
            "/user/delete/name/Petra",
            null,
            status().isBadRequest(),
            TestMessages.ACTOR_NOT_PRESENT),
        Arguments.of(
            "Actor not in database",
            false,
            "/user/delete/name/Petra",
            "ActorName",
            status().isForbidden(),
            String.format(ErrorMessages.USER_NOT_ALLOWED_DELETE_USER, "ActorName")),
        Arguments.of(
            "User to delete not in database ",
            false,
            "/user/delete/name/UserToDeleteNichtBekannt",
            "Torsten",
            status().isBadRequest(),
            String.format(ErrorMessages.USER_NOT_FOUND_NAME, "UserToDeleteNichtBekannt")),
        Arguments.of(
            "User to delete not present",
            false,
            "/user/delete/name/",
            "Torsten",
            status().isBadRequest(),
            TestMessages.USER_TO_DELETE_NOT_PRESENT),
        Arguments.of(
            "User is referenced in another table",
            true,
            "/user/delete/name/Petra",
            "Torsten",
            status().isInternalServerError(),
            String.format(ErrorMessages.USER_REFERENCED, "Petra")));
  }


        @ParameterizedTest(name = "{0}")
        @MethodSource("getDeleteUserByNameArguments")
        void testDeleteUserByName(
                        String testname,
                        Boolean createLog,
                        String url,
                        String actor,
                        ResultMatcher status,
                        String message)
                        throws Exception {
                if (createLog) {
                        logRepository.save(
                                        Log.builder()
                                                        .id(1)
                                                        .user(userList.get(0))
                                                        .message("Test")
                                                        .severity("INFO")
                                                        .timestamp(LocalDateTime.of(2000, 12, 12, 12, 12, 12))
                                                        .build());
                }
                MvcResult result = mockMvc
                                .perform(delete(url).param("actor", actor))
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
                        MvcResult result = mockMvc
                                        .perform(delete("/user/delete"))
                                        .andDo(print())
                                        .andExpect(status().isOk())
                                        .andReturn();

                        Assertions.assertEquals(
                                        InfoMessages.ALL_USERS_DELETED, result.getResponse().getContentAsString());
                }

                @Test
                void whenUserIsUsedSomewhereThenReturnCouldNotDelete() throws Exception {
                        logRepository.save(
                                        Log.builder()
                                                        .id(1)
                                                        .user(userList.get(0))
                                                        .message("Test")
                                                        .severity("INFO")
                                                        .timestamp(LocalDateTime.of(2000, 12, 12, 12, 12, 12))
                                                        .build());
                        MvcResult result = mockMvc
                                        .perform(delete("/user/delete"))
                                        .andDo(print())
                                        .andExpect(status().isInternalServerError())
                                        .andReturn();

                        Assertions.assertEquals(
                                        ErrorMessages.USERS_REFERENCED, result.getResponse().getContentAsString());
                }
        }

        private List<User> createUser() {
                List<User> userList = new ArrayList<>();
                User petra = User.builder()
                                .id(1)
                                .name("Petra")
                                .birthdate(LocalDate.of(1999, 12, 13))
                                .bmi(25.39)
                                .weight(65)
                                .height(1.60)
                                .favouriteColor("Red")
                                .build();
                userRepository.saveAndFlush(petra);
                User torsten = User.builder()
                                .name("Torsten")
                                .birthdate(LocalDate.of(1985, 12, 5))
                                .bmi(18.3)
                                .weight(61.3)
                                .height(1.83)
                                .id(2)
                                .favouriteColor("Blue")
                                .build();
                userRepository.saveAndFlush(torsten);
                User hans = User.builder()
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
