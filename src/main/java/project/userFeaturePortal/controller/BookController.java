package project.userFeaturePortal.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.userFeaturePortal.common.dto.books.BookRequestDto;
import project.userFeaturePortal.common.dto.books.BooksResponseDto;
import project.userFeaturePortal.common.message.InfoMessages;
import project.userFeaturePortal.model.entity.Book;
import project.userFeaturePortal.service.model.BookService;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookController {

  @Autowired
  private final BookService bookService;

  @GetMapping("/books")
  public ResponseEntity<BooksResponseDto> getAllBooks() {
    return ResponseEntity.status(HttpStatus.OK).body(new BooksResponseDto(bookService.getAllBooks(), null));
  }

  @PostMapping("/book")
  public ResponseEntity<BooksResponseDto> addBook(@RequestBody BookRequestDto parameters) {
    return ResponseEntity.status(HttpStatus.CREATED).body(new BooksResponseDto(bookService.addBook(
            parameters.erscheinungsjahr, parameters.titel, parameters.actor),
            String.format(InfoMessages.BOOK_CREATED, parameters.titel)));
  }

  @GetMapping("/book")
  public List<Book> findBooksByTitel(@RequestParam String titel) {
    return bookService.searchBooksByTitel(titel);
  }

  @DeleteMapping("/book/id/{id}")
  public ResponseEntity<BooksResponseDto> deleteBooksById(@PathVariable Integer id, @RequestParam String actor) {
    String returnMessage = bookService.deleteById(id, actor);
    return ResponseEntity.status(HttpStatus.OK).body(new BooksResponseDto(bookService.getAllBooks(), returnMessage));
  }

  @DeleteMapping("/book/titel")
  public String deleteBooksByTitel(@RequestParam String titel, @RequestParam String actor) {
    return bookService.deleteByTitel(titel, actor);
  }

  @DeleteMapping("/books")
  public String deleteAll(@RequestParam String actor) {
    return bookService.deleteBooks(actor);
  }
}
