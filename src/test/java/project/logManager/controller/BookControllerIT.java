package project.logManager.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.context.TestPropertySource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import project.logManager.common.message.ErrorMessages;
import project.logManager.common.message.InfoMessages;
import project.logManager.common.message.TestMessages;
import project.logManager.model.entity.Book;
import project.logManager.model.entity.Log;
import project.logManager.model.repository.BookRepository;
import project.logManager.model.repository.LogRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
@TestPropertySource("/application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookControllerIT {

        @Autowired
        private BookRepository bookRepository;

        @Autowired
        private MockMvc mockMvc;

        List<Book> books = new ArrayList<>();

        @BeforeAll
        public void setup() {
                books = creatBook();

        }

        private static Stream<Arguments> getAddBookArguments() {
                return Stream.of(
                                Arguments.of(
                                                "4",
                                                "haya",
                                                "1998",
                                                status().isOk(),
                                                String.format(InfoMessages.BOOK_CREATED, "haya")),
                                Arguments.of(
                                                "3",
                                                "petra",
                                                "1999",
                                                status().isOk(),
                                                String.format(InfoMessages.BOOK_CREATED, "petra")),
                                Arguments.of(
                                                "-1",
                                                "peter",
                                                "1988",
                                                status().isInternalServerError(),
                                                ErrorMessages.NO_BOOKS_FOUNDS),
                                Arguments.of(
                                                "lina",
                                                "1977",
                                                status().isBadRequest(),
                                                ErrorMessages.PARAMETER_IS_MISSING),
                                Arguments.of(
                                                "sara",
                                                "omar",
                                                "1888",
                                                status().isBadRequest(),
                                                ErrorMessages.PARAMETER_WRONG_FORMAT));
        }

        @ParameterizedTest(name = "add")
        @MethodSource("getAddBookArguments")
        void testAddBook(String id, String title, String release_year, String message, ResultMatcher status)
                        throws Exception {
                MvcResult result = mockMvc
                                .perform(
                                                get("/books")
                                                                .param("Title", title)
                                                                .param("ReleaseYear", release_year)
                                                                .param("ID", id))
                                .andDo(print())
                                .andExpect(status)
                                .andReturn();

                assertEquals(message, result.getResponse().getContentAsString());
        }

        @Nested
        class FindBookBytitelTests {
                @Test
                void testFindBookBytitel() throws Exception {
                        MvcResult result = mockMvc
                                        .perform(
                                                        get("/book/title")
                                                                        .param("title", "haya"))
                                        .andDo(print())
                                        .andExpect(status().isOk())
                                        .andReturn();

                        assertEquals(TestMessages.HAYA, result.getResponse().getContentAsString());
                }

                @Test
                void whenTitleToFindNotFoundThenReturnNull() throws Exception {
                        MvcResult result = mockMvc
                                        .perform(get("/book/title").param("title", "peter"))
                                        .andDo(print())
                                        .andExpect(status().isNotFound())
                                        .andReturn();

                        assertEquals("null", result.getResponse().getContentAsString());

                }
        }

        private static Stream<Arguments> getDeleteBookById() {
                return Stream.of(
                                Arguments.of(
                                                "14",
                                                "omar",
                                                "1234",
                                                status().isOk(),
                                                String.format(InfoMessages.BOOK_DELETED_ID, 14)),
                                Arguments.of(
                                                "16",
                                                "haya",
                                                "1966",
                                                status().isOk(),
                                                String.format(InfoMessages.BOOK_DELETED_ID, 12)),
                                Arguments.of(
                                                "16",
                                                "lina",
                                                "1977",
                                                status().isInternalServerError(),
                                                String.format(ErrorMessages.BOOK_NOT_FOUND_ID, 16)),
                                Arguments.of(
                                                "6",
                                                "peter",
                                                "1977",
                                                status().isInternalServerError(),
                                                String.format(ErrorMessages.BOOK_NOT_FOUND_ID, 6)));
        }

        @ParameterizedTest
        @MethodSource("getDeleteBookById")
        void testDeleteBookById(boolean bookIsReferenced,String actor ,ResultMatcher status , String message)
        throws Exception{
                )

        }

        private List<Book> creatBook() {

                List<Book> books = new ArrayList<>();
                Book haya = Book.builder()
                                .id(5)
                                .erscheinungsjahr(1789)
                                .titel("haya")
                                .build();
                bookRepository.save(haya);

                Book petra = Book.builder()
                                .id(7)
                                .erscheinungsjahr(1089)
                                .titel("petra")
                                .build();
                bookRepository.save(petra);

                Book peter = Book.builder()
                                .id(9)
                                .erscheinungsjahr(1589)
                                .titel("peter")
                                .build();
                bookRepository.save(peter);
                Book lina = Book.builder()
                                .id(12)
                                .erscheinungsjahr(2002)
                                .titel("lina")
                                .build();
                bookRepository.save(lina);
                Book omar = Book.builder()
                                .id(12)
                                .erscheinungsjahr(2002)
                                .titel("omar")
                                .build();
                bookRepository.save(omar);
                books.add(haya);
                books.add(petra);
                books.add(peter);
                books.add(lina);
                books.add(omar);
                return books;
        }

}
