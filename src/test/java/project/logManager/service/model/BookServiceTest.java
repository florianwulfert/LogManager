package project.logManager.service.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import project.logManager.common.message.InfoMessages;
import project.logManager.model.entity.Book;
import project.logManager.model.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    BookService bookService;

    @Mock
    BookRepository bookRepository;

    @Mock
    LogService logService;

    List<Book> books;

    @BeforeEach
    void init() {
        books = addTestBook();
    }

    @Test
    void testAddBook() {
        books.add(
                Book.builder()
                        .id(1)
                        .titel("haya")
                        .erscheinungsjahr(1998)
                        .build());
        bookService.addBook(books.get(0).getErscheinungsjahr(), books.get(0).getTitel(), "Torsten");
        verify(logService).addLog(any());
        verify(bookRepository).save(any());
    }

    @Test
    void testDeleteBooks() {
        assertEquals(InfoMessages.ALL_BOOKS_DELETED, bookService.deleteBooks());
        verify(bookRepository).deleteAll();
    }

    @Test
    void testDeleteById() {
        assertEquals(String.format(InfoMessages.BOOK_DELETED_ID, books.get(0).getId()),
                bookService.deleteById(books.get(0).getId(), "Torsten"));
        verify(bookRepository).delete(any());
    }

    @Test
    void testDeleteByTitelWhenBooksListIsEmptyReturnNoBooksFounds() {
        Mockito.when(bookRepository.findByTitel(books.get(0).getTitel())).thenReturn(new ArrayList<>());
        assertEquals(String.format(InfoMessages.NO_BOOKS_FOUNDS, books.get(0).getTitel()),
                bookService.deleteByTitel(books.get(0).getTitel(), "Torsten"));
    }

    @Test
    void testDeleteByTitel() {
        ArrayList<Book> bookz = new ArrayList<Book>();
        bookz.add(books.get(0));
        Mockito.when(bookRepository.findByTitel(books.get(0).getTitel())).thenReturn(bookz);
        assertEquals(String.format(InfoMessages.BOOK_DELETED_TITLE, books.get(0).getTitel()),
                bookService.deleteByTitel(books.get(0).getTitel(), "Torsten"));
        verify(logService).addLog(any());
    }

    @Test
    void testDeleteByTitelWhenMoreBooksWithSameTitelExistent() {
        Mockito.when(bookRepository.findByTitel(books.get(0).getTitel())).thenReturn(books);
        assertEquals(String.format(InfoMessages.BOOK_CAN_NOT_BE_IDENTIFIED, books.get(anyInt()).getTitel()),
                bookService.deleteByTitel(books.get(0).getTitel(), "Torsten"));
    }

    @Test
    void testGetAllBooks() {
        bookService.getAllBooks("Torsten");
        verify(logService).addLog(any());
        verify(bookRepository).findAll();
    }

    @Test
    void testSaveBook() {
        bookService.saveBook(any());
        verify(bookRepository).save(any());
    }

    @Test
    void testSaveLog() {
        bookService.saveLog("INFO", "string2", "Torsten");
        verify(logService).addLog(any());
    }

    @Test
    void testSearchBooksByTitel() {
        List<Book> books = new ArrayList<>();
        books.add(
                Book.builder()
                        .id(6)
                        .erscheinungsjahr(1999)
                        .titel("peter")
                        .build());
        books.add(
                Book.builder()
                        .id(7)
                        .erscheinungsjahr(1234)
                        .titel("petra")
                        .build());

        Mockito.when(bookRepository.findByTitel(anyString())).thenReturn(books);
        bookService.searchBooksByTitel(anyString());
        verify(bookRepository).findByTitel(anyString());
    }

    private List<Book> addTestBook() {
        List<Book> books = new ArrayList<>();
        books.add(
                Book.builder()
                        .id(1)
                        .erscheinungsjahr(1998)
                        .titel("haya")
                        .build());

        books.add(
                Book.builder()
                        .id(2)
                        .erscheinungsjahr(1900)
                        .titel("peter")
                        .build());
        books.add(
                Book.builder()
                        .id(3)
                        .erscheinungsjahr(1800)
                        .titel("petra")
                        .build());
        books.add(
                Book.builder()
                        .id(4)
                        .erscheinungsjahr(1888)
                        .titel("chris")
                        .build());
        books.add(
                Book.builder()
                        .id(5)
                        .erscheinungsjahr(1888)
                        .titel("chris")
                        .build());
        return books;
    }
}
