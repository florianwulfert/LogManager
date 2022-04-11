package project.userFeaturePortal.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.userFeaturePortal.common.dto.books.BooksResponseDto;
import project.userFeaturePortal.model.entity.Book;
import project.userFeaturePortal.service.model.BookService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@Tag(name = "BookComponent")
public class BookController {

  @Autowired
  private final BookService bookService;

  @GetMapping("/books")
  public BooksResponseDto getAllBooks(@RequestParam String actor) {
    return new BooksResponseDto(bookService.getAllBooks(actor), null);
  }

  @PostMapping("/book")
  public Book addBook(@RequestParam String titel, @RequestParam Integer erscheinungsjahr,
      @RequestParam String actor) {
    return bookService.addBook(erscheinungsjahr, titel, actor);
  }

  @GetMapping("/searchbook")
  public String findBooksBytitel(@RequestParam String titel) {
    return bookService.searchBooksByTitel(titel);
  }

  @DeleteMapping("/deletebookById/{id}")
  public String deleteBooksById(@PathVariable Integer id, @RequestParam String actor) {
    return bookService.deleteById(id, actor);
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