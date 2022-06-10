package project.userFeaturePortal.controller;

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
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import project.userFeaturePortal.TestMessages;
import project.userFeaturePortal.common.message.ErrorMessages;
import project.userFeaturePortal.common.message.InfoMessages;
import project.userFeaturePortal.model.entity.Book;
import project.userFeaturePortal.model.entity.Log;
import project.userFeaturePortal.model.entity.User;
import project.userFeaturePortal.model.repository.BookRepository;
import project.userFeaturePortal.model.repository.LogRepository;
import project.userFeaturePortal.model.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureDataJpa
@ComponentScan(basePackages = { "project.userFeaturePortal" })
@Transactional
@TestPropertySource("/application-user-test.properties")
@TestInstance(Lifecycle.PER_CLASS)
class UserControllerIT {

    List<User> userList = new ArrayList<>();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private BookRepository bookRepository;

    @BeforeAll
    public void setup() {
            userList = createUser();
    }

    private static Stream<Arguments> getAddUserArguments() {
    return Stream.of(
        Arguments.of(
            "User created",
            false,
            "{\"actor\":\"Petra\",\"name\":\"Hugo\",\"birthdate\":\"1999-12-13\",\"weight\":78.0,\"height\":1.8}",
            status().isCreated(),
            TestMessages.USER_CREATED_MESSAGE,
            Arguments.of(
                "First user has to create himself",
                true,
                "{\"actor\":\"Torsten\",\"name\":\"Hugo\",\"birthdate\":\"1995-11-05\",\"weight\":78.0,\"height\":1.8}",
                status().isInternalServerError(),
                ErrorMessages.NO_USERS_YET + "Hugo unequal Torsten"),
            Arguments.of(
                "First user created himself",
                true,
                "{\"actor\":\"Petra\",\"name\":\"Petra\",\"birthdate\":\"1995-11-05\",\"weight\":78.0,\"height\":1.8}",
                status().isOk(),
                String.format(InfoMessages.USER_CREATED + InfoMessages.BMI_MESSAGE, "Petra", 24.07)
                    + InfoMessages.NORMAL_WEIGHT),
            Arguments.of(
                "Actor not known",
                false,
                "{\"actor\":\"UnknownActor\",\"name\":\"Hugo\",\"birthdate\":\"1995-11-05\",\"weight\":78.0,\"height\":1.8}",
                status().isForbidden(),
                String.format(ErrorMessages.USER_NOT_ALLOWED_CREATE_USER, "UnknownActor")),
            Arguments.of(
                "Actor not given",
                false,
                "{\"name\":\"Hugo\",\"birthdate\":\"1995-11-05\",\"weight\":78.0,\"height\":1.8}",
                status().isBadRequest(),
                ErrorMessages.PARAMETER_IS_MISSING),
            Arguments.of(
                "Date has wrong format",
                false,
                "{\"actor\":\"Torsten\",\"name\":\"Hugo\",\"birthdate\":\"hallo\",\"weight\":78.0,\"height\":1.8}",
                status().isBadRequest(),
                ErrorMessages.ILLEGAL_BIRTHDATE_FORMAT),
            Arguments.of(
                "weight has wrong format",
                false,
                "{\"actor\":\"Torsten\",\"name\":\"Hugo\",\"birthdate\":\"1995-11-05\",\"weight\":\"hi\",\"height\":1.8}",
                status().isBadRequest(),
                ErrorMessages.PARAMETER_WRONG_FORMAT),
            Arguments.of(
                "height has wrong format",
                false,
                "{\"actor\":\"Torsten\",\"name\":\"Hugo\",\"birthdate\":\"1995-11-05\",\"weight\":\"78.0\",\"height\":\"hi\"}",
                status().isBadRequest(),
                ErrorMessages.PARAMETER_WRONG_FORMAT),
            Arguments.of(
                "User to create already exists",
                false,
                "{\"actor\":\"Torsten\",\"name\":\"Petra\",\"birthdate\":\"1995-11-05\",\"weight\":\"78.0\",\"height\":1.8}",
                status().isInternalServerError(),
                String.format(ErrorMessages.USER_EXISTS, "Petra")),
            Arguments.of(
                "UserNameNull",
                false,
                "{\"actor\":\"Torsten\",\"birthdate\":\"1995-11-05\",\"weight\":\"78.0\",\"height\":1.8}",
                status().isBadRequest(),
                ErrorMessages.PARAMETER_IS_MISSING),
            Arguments.of(
                "birthdateIsNull",
                false,
                "{\"actor\":\"Torsten\",\"name\":\"Hugo\",\"weight\":\"78.0\",\"height\":1.8}",
                status().isBadRequest(),
                ErrorMessages.PARAMETER_IS_MISSING),
            Arguments.of(
                "weightIsNull",
                false,
                "{\"actor\":\"Torsten\",\"name\":\"Hugo\",\"birthdate\":\"1995-11-05\",\"height\":1.8}",
                status().isBadRequest(),
                ErrorMessages.PARAMETER_IS_MISSING),
            Arguments.of(
                "heightIsNull",
                false,
                "{\"actor\":\"Torsten\",\"name\":\"Hugo\",\"birthdate\":\"1995-11-05\",\"weight\":\"78.0\"}",
                status().isBadRequest(),
                ErrorMessages.PARAMETER_IS_MISSING)));
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

            MvcResult result = mockMvc.perform(
                post("/user")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(content)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status)
                .andReturn();

            assertEquals(message, result.getResponse().getContentAsString());
    }

    private static Stream<Arguments> getUserUpdateArguments() {
        return Stream.of(
                Arguments.of(
                "User updated",
                        "{\"actor\":\"Petra\",\"name\":\"Torsten\",\"birthdate\":\"1999-12-13\",\"weight\":78.0,\"height\":1.8}",
                        status().isOk(),
                        TestMessages.USER_CREATED_MESSAGE),
                Arguments.of(
                        "Actor not known",
                        "{\"actor\":\"UnknownActor\",\"name\":\"Torsten\",\"birthdate\":\"1995-11-05\",\"weight\":78.0,\"height\":1.8}",
                        status().isForbidden(),
                        String.format(ErrorMessages.USER_NOT_ALLOWED_CREATE_USER, "UnknownActor")),
                Arguments.of(
                        "Actor not given",
                        "{\"name\":\"Torsten\",\"birthdate\":\"1995-11-05\",\"weight\":78.0,\"height\":1.8}",
                        status().isBadRequest(),
                        ErrorMessages.PARAMETER_IS_MISSING),
                Arguments.of(
                        "Date has wrong format",
                        "{\"actor\":\"Torsten\",\"name\":\"Torsten\",\"birthdate\":\"hallo\",\"weight\":78.0,\"height\":1.8}",
                        status().isBadRequest(),
                        ErrorMessages.ILLEGAL_BIRTHDATE_FORMAT),
                Arguments.of(
                        "weight has wrong format",
                        "{\"actor\":\"Torsten\",\"name\":\"Torsten\",\"birthdate\":\"1995-11-05\",\"weight\":\"hi\",\"height\":1.8}",
                        status().isBadRequest(),
                        ErrorMessages.PARAMETER_WRONG_FORMAT));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getUserUpdateArguments")
    void testUpdateUser(String testName, String content, ResultMatcher status) throws Exception {
        createUser();
        mockMvc
                .perform(post("/user/update")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status)
                .andReturn();
    }

    @Test
    void testFindUsers() throws Exception {
            MvcResult result = mockMvc.perform(get("/users"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            assertEquals(TestMessages.PETRA_TORSTEN_HANS, result.getResponse().getContentAsString());
    }

    @Nested
    class AddFavouriteBookTests {
        @Test
        void testAddBookToUser() throws Exception {
                createBook();
                MvcResult result = mockMvc
                                .perform(post("/user/favouriteBook").param("bookTitel", "TestBook").param("actor", "Torsten"))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andReturn();
                assertEquals(TestMessages.TESTBOOK_TO_TORSTEN, result.getResponse().getContentAsString());
        }

        @Test
        void WhenFavouriteBookToAddNotFound() throws Exception {
                createBook();
                MvcResult result = mockMvc
                                .perform(post("/user/favouriteBook").param("bookTitel", "Florian").param("actor",
                                                "Torsten"))
                                .andDo(print())
                                .andExpect(status().isInternalServerError())
                                .andReturn();

                assertEquals(String.format(ErrorMessages.BOOK_NOT_FOUND_TITEL, "Florian"),
                                result.getResponse().getContentAsString());
        }
    }

    @Test
    void testFindUserByName() throws Exception {
            mockMvc.perform(get("/user/")
                            .param("name", "Torsten")).andDo(print()).andExpect(status().isOk()).andReturn();
    }

    private static Stream<Arguments> getDeleteUserByIdArguments() {
            return Stream.of(
                            Arguments.of(
                                            false,
                                            "/user/id/1",
                                            "Hans",
                                            status().isOk(),
                                            TestMessages.USER_DELETED_BY_ID),
                            Arguments.of(
                                            false,
                                            "/user/id/1",
                                            "Paul",
                                            status().isForbidden(),
                                            String.format(ErrorMessages.USER_NOT_ALLOWED_DELETE_USER, "Paul")),
                            Arguments.of(
                                            false, "/user/id/1", null, status().isBadRequest(),
                                            TestMessages.ACTOR_NOT_PRESENT),
                            Arguments.of(
                                            false,
                                            "/user/id/8",
                                            "Torsten",
                                            status().isInternalServerError(),
                                            String.format(ErrorMessages.USER_NOT_FOUND_ID, 8)),
                            Arguments.of(
                                            false,
                                            "/user/id/2",
                                            "Torsten",
                                            status().isInternalServerError(),
                                            ErrorMessages.USER_DELETE_HIMSELF),
                            Arguments.of(
                                            true,
                                            "/user/id/1",
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

            assertEquals(message, result.getResponse().getContentAsString());
    }

    private static Stream<Arguments> getDeleteUserByNameArguments() {
            return Stream.of(
                Arguments.of(
                                "User successfully deleted by name",
                                false,
                                "/user/name/Petra",
                                "Torsten",
                                status().isOk(),
                                TestMessages.USER_PETRA_DELETED_BY_NAME),
                Arguments.of(
                                "Actor wants to delete himself",
                                false,
                                "/user/name/Torsten",
                                "Torsten",
                                status().isInternalServerError(),
                                ErrorMessages.USER_DELETE_HIMSELF),
                Arguments.of(
                                "Actor not present",
                                false,
                                "/user/name/Petra",
                                null,
                                status().isBadRequest(),
                                TestMessages.ACTOR_NOT_PRESENT),
                Arguments.of(
                                "Actor not in database",
                                false,
                                "/user/name/Petra",
                                "ActorName",
                                status().isForbidden(),
                                String.format(ErrorMessages.USER_NOT_ALLOWED_DELETE_USER, "ActorName")),
                Arguments.of(
                                "User to delete not in database ",
                                false,
                                "/user/name/UserToDeleteNichtBekannt",
                                "Torsten",
                                status().isNotFound(),
                                String.format(ErrorMessages.USER_NOT_FOUND_NAME,
                                                "UserToDeleteNichtBekannt")),
                Arguments.of(
                                "User is referenced in another table",
                                true,
                                "/user/name/Petra",
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

            assertEquals(message, result.getResponse().getContentAsString());
    }

    private void createBook() {
        Book testBook = Book.builder()
                .id(1)
                .erscheinungsjahr(1998)
                .titel("TestBook")
                .build();
        bookRepository.save(testBook);
    }
  private List<User> createUser() {
    List<User> userList = new ArrayList<>();
    User petra =
        User.builder()
            .id(1)
            .name("Petra")
            .birthdate(LocalDate.of(1999, 12, 13))
            .bmi(25.39)
            .weight(65)
            .height(1.60)
            .build();
    userRepository.saveAndFlush(petra);
    User torsten =
        User.builder()
            .name("Torsten")
            .birthdate(LocalDate.of(1985, 12, 5))
            .bmi(18.3)
            .weight(61.3)
            .height(1.83)
            .id(2)
            .build();
    userRepository.saveAndFlush(torsten);
    User hans =
        User.builder()
            .name("Hans")
            .birthdate(LocalDate.of(1993, 2, 3))
            .bmi(22.11)
            .weight(75.7)
            .height(1.85)
            .id(3)
            .build();
    userList.add(petra);
    userList.add(torsten);
    userList.add(hans);
    userRepository.save(petra);
    userRepository.save(torsten);
    userRepository.save(hans);
    return userList;
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

                    assertEquals(TestMessages.PETRA, result.getResponse().getContentAsString());
            }

            @Test
            void whenIdToFindIsNullThenReturnBadRequest() throws Exception {
                    MvcResult result = mockMvc
                                    .perform(get("/user/id"))
                                    .andDo(print())
                                    .andExpect(status().isBadRequest())
                                    .andReturn();

                    assertEquals(
                                    ErrorMessages.ID_NOT_PRESENT, result.getResponse().getContentAsString());
            }

            @Test
            void whenIdToFindNotFoundThenReturnNull() throws Exception {
                    MvcResult result = mockMvc
                                    .perform(get("/user/id").param("id", "50"))
                                    .andDo(print())
                                    .andExpect(status().isOk())
                                    .andReturn();

                    assertEquals("null", result.getResponse().getContentAsString());
            }
    }

    @Nested
    class DeleteAllTests {

            @Test
            void testDeleteAll() throws Exception {
                    logRepository.deleteAll();
                    MvcResult result = mockMvc
                                    .perform(delete("/users"))
                                    .andDo(print())
                                    .andExpect(status().isOk())
                                    .andReturn();

                    assertEquals(TestMessages.EMPTY_LIST, result.getResponse().getContentAsString());
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
                                    .perform(delete("/users"))
                                    .andDo(print())
                                    .andExpect(status().isInternalServerError())
                                    .andReturn();

                    assertEquals(
                                    ErrorMessages.USERS_REFERENCED, result.getResponse().getContentAsString());
            }
    }
}
