package project.userFeaturePortal.service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.userFeaturePortal.common.message.InfoMessages;
import project.userFeaturePortal.model.entity.Book;
import project.userFeaturePortal.model.repository.BookRepository;
import project.userFeaturePortal.model.repository.UserRepository;
import project.userFeaturePortal.service.validation.BookValidationService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

  @InjectMocks
  BookService bookService;

  @Mock
  BookRepository bookRepository;

  @Mock
  LogService logService;

  @Mock
  UserRepository userRepository;

  @Mock
  BookValidationService bookValidationService;

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
    assertEquals(InfoMessages.ALL_BOOKS_DELETED, bookService.deleteBooks(anyString()));
    verify(bookRepository).deleteAll();
  }

  @Test
  void testDeleteById() {
    assertEquals(String.format(InfoMessages.BOOK_DELETED_ID, books.get(0).getId()),
        bookService.deleteById(books.get(0).getId(), "Torsten"));
    verify(bookRepository).deleteById(any());
  }

  @Test
  void whenBooksListIsEmpty_ThenReturnNoBooksFounds() {
    when(bookRepository.findByTitel(books.get(0).getTitel())).thenReturn(new ArrayList<>());
    assertEquals(String.format(InfoMessages.NO_BOOKS_FOUNDS, books.get(0).getTitel()),
        bookService.deleteByTitel(books.get(0).getTitel(), "Torsten"));
  }

  @Test
  void testDeleteByTitel() {
    List<Book> testBooks = new ArrayList<>();
    testBooks.add(Book.builder().titel("TestBook").erscheinungsjahr(2002).build());
    when(bookRepository.findByTitel(anyString())).thenReturn(testBooks);
    assertEquals(String.format(InfoMessages.BOOK_DELETED_TITLE, books.get(0).getTitel()),
            bookService.deleteByTitel(books.get(0).getTitel(), "Torsten"));
    verify(logService).addLog(any());
  }

  @Test
  void testDeleteByTitleWhenMoreBooksWithSameTitleExist() {
    when(bookRepository.findByTitel(books.get(0).getTitel())).thenReturn(books);
    assertEquals(String.format(InfoMessages.BOOK_CAN_NOT_BE_IDENTIFIED, books.get(anyInt()).getTitel()),
        bookService.deleteByTitel(books.get(0).getTitel(), "Torsten"));
  }

  @Test
  void testGetAllBooks() {
    List<Book> books = addTestBook();
    when(bookRepository.findAll()).thenReturn(books);
    bookService.getAllBooks();
    verify(bookRepository).findAll();
  }


  @Test
  void testSearchBooksByTitel() {
    when(bookRepository.findByTitel(anyString())).thenReturn(books);
    bookService.searchBooksByTitel("haya");
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
