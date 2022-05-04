package project.userFeaturePortal.service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.userFeaturePortal.common.message.InfoMessages;
import project.userFeaturePortal.model.entity.Book;
import project.userFeaturePortal.model.entity.User;
import project.userFeaturePortal.model.repository.BookRepository;
import project.userFeaturePortal.model.repository.UserRepository;
import project.userFeaturePortal.service.validation.BookValidationService;
import project.userFeaturePortal.service.validation.UserValidationService;

import java.time.LocalDate;
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

  @Mock
  UserValidationService userValidationService;

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
    User user = User.builder()
            .id(1)
            .name("Peter")
            .birthdate(LocalDate.of(2005, 12, 12))
            .weight(90.0)
            .height(1.85)
            .bmi(26.29)
            .favouriteBook(null)
            .build();
    when(userValidationService.checkIfNameExists(anyString(), anyBoolean(), anyString())).thenReturn(user);
    assertEquals(InfoMessages.ALL_BOOKS_DELETED, bookService.deleteBooks("Peter"));
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
    User user = User.builder()
            .id(1)
            .name("Peter")
            .birthdate(LocalDate.of(2005, 12, 12))
            .weight(90.0)
            .height(1.85)
            .bmi(26.29)
            .favouriteBook(null)
            .build();
    when(userValidationService.checkIfNameExists(anyString(),anyBoolean(),anyString())).thenReturn(user);
    List<Book> testBooks = new ArrayList<>();
    testBooks.add(Book.builder().id(1).titel("TestBook").erscheinungsjahr(2002).build());
    when(bookRepository.findByTitel(anyString())).thenReturn(testBooks);
    when(bookValidationService.checkIfBookIsReferenced(any())).thenReturn()
    assertEquals(String.format(InfoMessages.BOOK_DELETED_TITLE, "TestBook"),
            bookService.deleteByTitel("TestBook", "Torsten"));
    verify(logService).addLog(any());
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
