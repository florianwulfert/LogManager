package project.userFeaturePortal.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
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
import project.userFeaturePortal.common.message.InfoMessages;
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
  @Autowired private BookRepository bookRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private MockMvc mockMvc;

  private static Stream<Arguments> getAddBookArguments() {
    return Stream.of(
        Arguments.of(
            "{\"titel\":\"haya\",\"erscheinungsjahr\":1998,\"actor\":\"Torsten\"}",
            status().isOk(),
            String.format(TestMessages.HAYA, "haya")),
        Arguments.of(
            "{\"titel\":\"petra\",\"erscheinungsjahr\":\"1999\",\"actor\":\"Torsten\"}",
            status().isOk(),
            String.format(TestMessages.PETRA_BOOK, "petra")),
        Arguments.of(
            "{\"titel\":\"peter\",\"erscheinungsjahr\":1988}",
            status().isNotFound(),
            String.format(ErrorMessages.USER_NOT_FOUND_NAME, "notFoundUser")),
        Arguments.of(
            "{\"erscheinungsjahr\":1977,\"actor\":\"Torsten\"}",
            status().isBadRequest(),
            ErrorMessages.PARAMETER_IS_MISSING),
        Arguments.of(
            "{\"titel\":\"omar\",\"actor\":\"Torsten\"}",
            status().isBadRequest(),
            ErrorMessages.PARAMETER_IS_MISSING));
  }

  private static Stream<Arguments> getDeleteBookById() {
    return Stream.of(
        Arguments.of(
            "petra",
            "/deletebookById/1",
            "Torsten",
            status().isOk(),
            TestMessages.BOOK_DELETED_PETRA),
        Arguments.of(
            "peter",
            "/deletebookById/4",
            "Torsten",
            status().isOk(),
            TestMessages.BOOK_DELETED_PETER),
        Arguments.of(
            "haya",
            "/deletebookById/kevin",
            "Torsten",
            status().isBadRequest(),
            String.format(TestMessages.ID_FOR_BOOK_HAS_WRONG_FORMAT)),
        Arguments.of("omar", "/deletebookById/", "Torsten", status().isNotFound(), ""));
  }

  private static Stream<Arguments> getDeleteBookByTitelArguments() {
    return Stream.of(
        Arguments.of(
            "BookComponent was succussfuly deleted",
            "peter",
            "Torsten",
            status().isOk(),
            String.format(InfoMessages.BOOK_CAN_NOT_BE_IDENTIFIED, "peter")),
        Arguments.of(
            "BookComponent was succussfuly deleted",
            "omar",
            "Torsten",
            status().isOk(),
            String.format(InfoMessages.BOOK_DELETED_TITLE, "omar")),
        Arguments.of(
            "Actor is not present ",
            null,
            "Actor",
            status().isBadRequest(),
            ErrorMessages.TITLE_IS_NOT_PRESENT),
        Arguments.of(
            "BookComponent to delete is not in database ",
            "hajer",
            "Torsten",
            status().isOk(),
            String.format(InfoMessages.NO_BOOKS_FOUNDS, "hajer")),
        Arguments.of(
            "There are more books with the title paul",
            "paul",
            "Torsten",
            status().isOk(),
            String.format(InfoMessages.BOOK_CAN_NOT_BE_IDENTIFIED, "paul")));
  }

  @BeforeAll
  public void setup() {
    books = creatBook();
    User user =
        User.builder()
            .name("Torsten")
            .birthdate(LocalDate.now())
            .height(1.8)
            .weight(90)
            .favouriteColor("green")
            .build();
    userRepository.save(user);
  }

  @Test
  void testGetBooks() throws Exception {
    bookRepository.findAll();
      mockMvc
            .perform(get("/books").param("actor", "Torsten"))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
  }

  @ParameterizedTest(name = "{2}")
  @MethodSource("getAddBookArguments")
  void testAddBook(String content, ResultMatcher status, String message) throws Exception {
    creatBook();
    MvcResult result =
        mockMvc
            .perform(
                post("/book")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(content)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status)
            .andReturn();

        assertEquals(message, result.getResponse().getContentAsString());
    }

  @ParameterizedTest(name = "{0}")
  @MethodSource("getDeleteBookById")
  void testDeleteBookById(
      String titel, String url, String actor, ResultMatcher status, String message)
      throws Exception {
    MvcResult result =
        mockMvc
            .perform(delete(url).param("actor", actor))
            .andDo(print())
            .andExpect(status)
            .andReturn();
    assertEquals(message, result.getResponse().getContentAsString());
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("getDeleteBookByTitelArguments")
  void TestDeleteBookByTitel(
      String zustand, String titel, String actor, ResultMatcher status, String message)
      throws Exception {
    MvcResult result =
        mockMvc
            .perform(delete("/deletebooksByTitel").param("titel", titel).param("actor", actor))
            .andDo(print())
            .andExpect(status)
            .andReturn();

        assertEquals(message, result.getResponse().getContentAsString());
    }

    @Test
    void testDeleteAll() throws Exception {

    MvcResult result =
        mockMvc
            .perform(delete("/allBooksdelete"))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

    assertEquals(InfoMessages.ALL_BOOKS_DELETED, result.getResponse().getContentAsString());
  }

    private List<Book> creatBook() {

    List<Book> books = new ArrayList<>();
    Book haya = Book.builder().id(9).erscheinungsjahr(1998).titel("haya").build();
    bookRepository.saveAndFlush(haya);

    Book petra = Book.builder().id(4).erscheinungsjahr(1989).titel("petra").build();
    bookRepository.saveAndFlush(petra);

    Book peter = Book.builder().id(10).erscheinungsjahr(2010).titel("peter").build();
    bookRepository.saveAndFlush(peter);
    Book lina = Book.builder().id(12).erscheinungsjahr(2009).titel("lina").build();
    bookRepository.saveAndFlush(lina);
    Book omar = Book.builder().id(13).erscheinungsjahr(2002).titel("omar").build();
    bookRepository.saveAndFlush(omar);
    Book paul = Book.builder().id(14).erscheinungsjahr(2002).titel("paul").build();
    bookRepository.saveAndFlush(paul);
    Book paul1 = Book.builder().id(15).erscheinungsjahr(2008).titel("paul").build();
    bookRepository.saveAndFlush(paul1);
    books.add(haya);
    books.add(petra);
    books.add(peter);
    books.add(lina);
    books.add(omar);
    books.add(paul);
    books.add(paul1);
    bookRepository.save(paul);
    bookRepository.save(paul1);
    bookRepository.save(omar);
    bookRepository.save(lina);
    bookRepository.save(peter);
    bookRepository.save(petra);
    bookRepository.save(haya);
    return books;
  }
        List<Book> books = new ArrayList<>();
        Book haya = Book.builder()
                .id(1)
                .erscheinungsjahr(1998)
                .titel("haya")
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
        Book paul1 = Book.builder()
                .id(7)
                .erscheinungsjahr(2008)
                .titel("paul")
                .build();
        books.add(paul1);
        bookRepository.save(paul1);
        return books;
    }

    @Nested
    class FindBookBytitelTests {

    @Test
    void testFindBookBytitel() throws Exception {
      MvcResult result =
          mockMvc
              .perform(get("/searchbook").param("titel", "haya"))
              .andDo(print())
              .andExpect(status().isOk())
              .andReturn();
      assertEquals(
          String.format(InfoMessages.Book_FOUND_TITLE, "haya"),
          result.getResponse().getContentAsString());
    }

    @Test
    void whenTitleToFindNotFoundThenReturnIsNotFound() throws Exception {
      MvcResult result =
          mockMvc
              .perform(get("/book/title").param("title", "christina"))
              .andDo(print())
              .andExpect(status().isNotFound())
              .andReturn();

      assertEquals("", result.getResponse().getContentAsString());
    }
  }
}
