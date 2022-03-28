package project.logManager.controller;

import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import project.logManager.service.model.BookService;
import project.logManager.model.entity.Book;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    @InjectMocks
    BookController bookController;

    @Mock
    BookService bookService;

    List<Book> books;

    @BeforeEach
    void init() {
        books = addTestBook();
    }

    @Test
    void testAddBook() {
        books.add(
                Book.builder()
                        .id(6)
                        .titel("petra")
                        .erscheinungsjahr(1990)
                        .build());
        bookController.addBook(books.get(0).getTitel(), books.get(0).getErscheinungsjahr(), "Torsten");
        verify(bookService).addBook(1998, "haya", "Torsten");
    }

    @Test
    void testDeleteAll() {
        bookController.deleteAll();
        verify(bookService).deleteBooks();
    }

    @Test
    void testDeleteBooksById() {
        bookController.deleteBooksById(books.get(0).getId(), "Torsten");
        verify(bookService).deleteById(2, "Torsten");
    }

    @Test
    void testDeleteBooksByTitel() {
        bookController.deleteBooksByTitel(books.get(0).getTitel(), "Torsten");
        verify(bookService).deleteByTitel("haya", "Torsten");
    }

    @Test
    void testFindBooksBytitel() {
        bookController.findBooksBytitel("haya");
        verify(bookService).searchBooksByTitel("haya");

    }

    @Test
    void testGetAllBooks() {
        bookController.getAllBooks("Torsten");
        verify(bookService).getAllBooks("Torsten");
    }

    private List<Book> addTestBook() {
        List<Book> books = new ArrayList<>();
        books.add(
                Book.builder()
                        .id(2)
                        .titel("haya")
                        .erscheinungsjahr(1998)
                        .build());

        books.add(
                Book.builder()
                        .id(3)
                        .titel("Lina")
                        .erscheinungsjahr(2000)
                        .build());
        books.add(
                Book.builder()
                        .id(4)
                        .titel("Chris")
                        .erscheinungsjahr(2002)
                        .build());
        return books;
    }
}
