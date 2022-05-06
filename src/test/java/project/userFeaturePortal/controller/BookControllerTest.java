package project.userFeaturePortal.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.userFeaturePortal.common.dto.books.BookRequestDto;
import project.userFeaturePortal.model.entity.Book;
import project.userFeaturePortal.service.model.BookService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

  @InjectMocks BookController bookController;

  @Mock BookService bookService;

  List<Book> books;

  @BeforeEach
  void init() {
    books = addTestBook();
  }

  @Test
  void testAddBook() {
    books.add(Book.builder().id(6).titel("petra").erscheinungsjahr(1990).build());
    BookRequestDto test =
        BookRequestDto.builder()
            .actor("Torsten")
            .erscheinungsjahr(books.get(0).getErscheinungsjahr())
            .titel(books.get(0).getTitel())
            .build();
    bookController.addBook(test);
    verify(bookService).addBook(1998, "haya", "Torsten");
  }

  @Test
  void testDeleteAll() {
    bookController.deleteAll(anyString());
    verify(bookService).deleteBooks(anyString());
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
    bookController.findBooksByTitel("haya");
    verify(bookService).searchBooksByTitel("haya");
  }

  @Test
  void testGetAllBooks() {
    bookController.getAllBooks();
    verify(bookService).getAllBooks();
  }

  private List<Book> addTestBook() {
    List<Book> books = new ArrayList<>();
    books.add(Book.builder().id(2).titel("haya").erscheinungsjahr(1998).build());

    books.add(Book.builder().id(3).titel("Lina").erscheinungsjahr(2000).build());
    books.add(Book.builder().id(4).titel("Chris").erscheinungsjahr(2002).build());
    return books;
  }
}
