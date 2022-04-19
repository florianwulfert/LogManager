package project.userFeaturePortal.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.userFeaturePortal.common.dto.books.AddBookResponseDto;
import project.userFeaturePortal.common.dto.books.BookRequestDto;
import project.userFeaturePortal.common.dto.books.BooksResponseDto;
import project.userFeaturePortal.service.model.BookService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@Tag(name = "BookComponent")
public class BookController {

  @Autowired
  private final BookService bookService;

  @GetMapping("/books")
  public BooksResponseDto getAllBooks() {
    return new BooksResponseDto(bookService.getAllBooks(), null);
  }

  @PostMapping("/book")
  public AddBookResponseDto addBook(@RequestBody BookRequestDto parameters) {
    return new AddBookResponseDto(bookService.addBook(parameters.erscheinungsjahr, parameters.titel, parameters.actor));
  }

  @GetMapping("/searchbook")
  public String findBooksByTitel(@RequestParam String titel) {
    return bookService.searchBooksByTitel(titel);
  }

  @DeleteMapping("/deletebookById/{id}")
  public BooksResponseDto deleteBooksById(@PathVariable Integer id, @RequestParam String actor) {
    String returnMessage = bookService.deleteById(id, actor);
    return new BooksResponseDto(bookService.getAllBooks(), returnMessage);
  }

  @DeleteMapping("/deletebooksByTitel")
  public String deleteBooksByTitel(@RequestParam String titel, @RequestParam String actor) {
    return bookService.deleteByTitel(titel, actor);
  }

  @DeleteMapping("allBooksdelete")
  public String deleteAll() {
    return bookService.deleteBooks();
  }
}
