package project.userFeaturePortal.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import project.userFeaturePortal.model.entity.Book;
import project.userFeaturePortal.model.entity.User;
import project.userFeaturePortal.model.repository.BookRepository;
import project.userFeaturePortal.model.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
@AutoConfigureDataJpa
@ComponentScan(basePackages = { "project.userFeaturePortal" })
@Transactional
@TestPropertySource("/application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookControllerIT {

    List<Book> books = new ArrayList<>();
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;

    private static Stream<Arguments> getAddBookArguments() {
    return Stream.of(
        Arguments.of(
            "{\"titel\":\"TestBook1\",\"erscheinungsjahr\":\"1998\",\"actor\":\"Torsten\"}",
            status().isCreated(),
            TestMessages.TESTBOOK_CREATED),
        Arguments.of(
            "{\"titel\":\"peter\",\"erscheinungsjahr\":\"1988\"}",
            status().isForbidden(),
            ErrorMessages.USER_NOT_ALLOWED,
        Arguments.of(
            "{\"erscheinungsjahr\":\"1977\",\"actor\":\"Torsten\"}",
            status().isBadRequest(),
            ErrorMessages.PARAMETER_IS_MISSING),
        Arguments.of(
            "{\"titel\":\"omar\",\"actor\":\"Torsten\"}",
            status().isBadRequest(),
            ErrorMessages.PARAMETER_IS_MISSING)));
    }

    private static Stream<Arguments> getDeleteBookById() {
        return Stream.of(
                Arguments.of(
                        "petra",
                        "/book/id/1",
                        "Torsten",
                        status().isOk(),
                        TestMessages.BOOK_PETRA_DELETED_BY_ID),
                Arguments.of(
                        "peter",
                        "/book/id/3",
                        "Torsten",
                        status().isOk(),
                        TestMessages.BOOK_PETER_DELETED_BY_ID),
                Arguments.of(
                        "haya",
                        "/book/id/kevin",
                        "Torsten",
                        status().isBadRequest(),
                        String.format(TestMessages.ID_FOR_BOOK_HAS_WRONG_FORMAT)),
                Arguments.of(
                        "omar",
                        "/book/id/",
                        "Torsten",
                        status().isNotFound(),
                        ""));
    }

    private static Stream<Arguments> getDeleteBookByTitelArguments() {
        return Stream.of(
                Arguments.of(
                        "Book was successfully deleted",
                        "TestBook",
                        "Torsten",
                        status().isOk(),
                        TestMessages.TESTBOOK_DELETED_BY_TITLE),
                Arguments.of(
                        "Actor is not present ",
                        null,
                        "Actor",
                        status().isBadRequest(),
                        ErrorMessages.TITLE_IS_NOT_PRESENT),
                Arguments.of(
                        "Book to delete is not in database ",
                        "hajer",
                        "Torsten",
                        status().isOk(),
                        TestMessages.NO_BOOKS_FOUND));
    }

  @BeforeAll
  public void setup() {
    books = creatBook();
    User user = User.builder().name("Torsten").birthdate(LocalDate.now()).height(1.8).weight(90).build();
    userRepository.save(user);
  }

    @Test
    void testGetBooks() throws Exception {
        MvcResult result = mockMvc
                .perform(get("/books").param("actor", "Torsten"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String booksString = books.toString().replace(" ", "");
    assertEquals(
        TestMessages.GET_BOOKS,
        result.getResponse().getContentAsString());
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("getAddBookArguments")
    void testAddBook(
            String testData,
            ResultMatcher status,
            String message)
            throws Exception {
        MvcResult result = mockMvc
                .perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(testData)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status)
                .andReturn();

        assertEquals(message, result.getResponse().getContentAsString());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getDeleteBookById")
    void testDeleteBookById(
            String titel,
            String url,
            String actor,
            ResultMatcher status,
            String message)
            throws Exception {
        MvcResult result = mockMvc
                .perform(delete(url).param("actor", actor))
                .andDo(print())
                .andExpect(status)
                .andReturn();
        assertEquals(message, result.getResponse().getContentAsString());

    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getDeleteBookByTitelArguments")
    void TestDeleteBookByTitel(
            String zustand,
            String titel,
            String actor,
            ResultMatcher status,
            String message) throws Exception {
        MvcResult result = mockMvc
                .perform(delete("/book/titel").param("titel", titel).param("actor", actor))
                .andDo(print())
                .andExpect(status)
                .andReturn();

        assertEquals(message, result.getResponse().getContentAsString());
    }

    @Test
    void testDeleteAll() throws Exception {

        MvcResult result = mockMvc
                .perform(delete("/books")
                        .param("actor", "Torsten"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(TestMessages.ALL_BOOKS_DELETED, result.getResponse().getContentAsString());

    }

    private List<Book> creatBook() {

        List<Book> books = new ArrayList<>();
        Book haya = Book.builder()
                .id(1)
                .erscheinungsjahr(1998)
                .titel("TestBook")
                .build();
        books.add(haya);
        bookRepository.save(haya);
        Book petra = Book.builder()
                .id(2)
                .erscheinungsjahr(1989)
                .titel("petra")
                .build();
        books.add(petra);
        bookRepository.save(petra);
        Book peter = Book.builder()
                .id(3)
                .erscheinungsjahr(2010)
                .titel("peter")
                .build();
        books.add(peter);
        bookRepository.save(peter);
        Book lina = Book.builder()
                .id(4)
                .erscheinungsjahr(2009)
                .titel("lina")
                .build();
        books.add(lina);
        bookRepository.save(lina);
        Book omar = Book.builder()
                .id(5)
                .erscheinungsjahr(2002)
                .titel("omar")
                .build();
        books.add(omar);
        bookRepository.save(omar);
        Book paul = Book.builder()
                .id(6)
                .erscheinungsjahr(2002)
                .titel("paul")
                .build();
        books.add(paul);
        bookRepository.save(paul);
        return books;
    }

    @Test
    void testFindBookBytitel() throws Exception {
        MvcResult result = mockMvc
                .perform(get("/book").param("titel", "TestBook"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(String.format(TestMessages.TESTBOOK),
                result.getResponse().getContentAsString());
    }
}
