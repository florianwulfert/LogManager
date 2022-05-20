package project.userFeaturePortal.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import project.userFeaturePortal.common.dto.books.BookRequestDto;
import project.userFeaturePortal.common.dto.books.BooksResponseDto;
import project.userFeaturePortal.common.dto.books.FindBookResponseDto;
import project.userFeaturePortal.common.message.InfoMessages;
import project.userFeaturePortal.controller.API.BookAPI;
import project.userFeaturePortal.service.model.BookService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookController implements BookAPI {

  @Autowired private final BookService bookService;

  @Override
  public ResponseEntity<BooksResponseDto> getAllBooks() {
    return ResponseEntity.status(HttpStatus.OK)
        .body(new BooksResponseDto(bookService.getAllBooks(), null));
  }

  @Override
  public ResponseEntity<BooksResponseDto> addBook(BookRequestDto parameters) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            new BooksResponseDto(
                bookService.addBook(
                    parameters.erscheinungsjahr, parameters.titel, parameters.actor),
                String.format(InfoMessages.BOOK_CREATED, parameters.titel)));
  }

  @Override
  public ResponseEntity<BooksResponseDto> updateBook(BookRequestDto allParameters) {
    String returnMessage = bookService.updateBook(allParameters.titel, allParameters.erscheinungsjahr, allParameters.actor);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new BooksResponseDto(bookService.getAllBooks(), returnMessage));
  }

  @Override
  public ResponseEntity<FindBookResponseDto> findBooksByTitel(String titel) {
    return ResponseEntity.status(HttpStatus.OK).body(new FindBookResponseDto(bookService.searchBooksByTitel(titel)));
  }

  @Override
  public ResponseEntity<BooksResponseDto> deleteBooksById(Integer id, String actor) {
    String returnMessage = bookService.deleteById(id, actor);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new BooksResponseDto(bookService.getAllBooks(), returnMessage));
  }

  @Override
  public ResponseEntity<BooksResponseDto> deleteBooksByTitel(String titel, String actor) {
    String returnMessage = bookService.deleteByTitel(titel, actor);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new BooksResponseDto(bookService.getAllBooks(), returnMessage));
  }

  @Override
  public ResponseEntity<BooksResponseDto> deleteAll(String actor) {
    String returnMessage = bookService.deleteBooks(actor);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new BooksResponseDto(bookService.getAllBooks(), returnMessage));
  }
}
