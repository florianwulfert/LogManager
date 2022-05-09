package project.userFeaturePortal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
  @Operation(
          summary = "Get list of books",
          responses = {
                  @ApiResponse(
                          description = "Get books succeeded",
                          responseCode = "200",
                          content =
                          @Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = BooksResponseDto.class)))
          })
  public ResponseEntity<BooksResponseDto> getAllBooks() {
    return ResponseEntity.status(HttpStatus.OK).body(new BooksResponseDto(bookService.getAllBooks(), null));
  }

  @PostMapping("/book")
  @Operation(
          summary = "Add manually a new Book",
          responses = {
                  @ApiResponse(
                          description = "Adding a book succeeded",
                          responseCode = "201",
                          content =
                          @Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = BooksResponseDto.class))),
                  @ApiResponse(
                          description = "Creating user not found",
                          responseCode = "404",
                          content =
                          @Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = BooksResponseDto.class))),
                  @ApiResponse(
                          description = "User is not allowed to add a book",
                          responseCode = "403",
                          content =
                          @Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = BooksResponseDto.class))),
                  @ApiResponse(
                          description = "One of the parameters has wrong format.",
                          responseCode = "400",
                          content =
                          @Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = BooksResponseDto.class)))
          })
  public ResponseEntity<BooksResponseDto> addBook(@RequestBody BookRequestDto parameters) {
    return ResponseEntity.status(HttpStatus.CREATED).body(new BooksResponseDto(bookService.addBook(
            parameters.erscheinungsjahr, parameters.titel, parameters.actor),
            String.format(InfoMessages.BOOK_CREATED, parameters.titel)));
  }

  @GetMapping("/book")
  @Operation(summary = "Found books by titel of the book",
          responses = {
                  @ApiResponse(
                          description = "Book(s) found",
                          responseCode = "200",
                          content =
                          @Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = BooksResponseDto.class))),
          })
  public List<Book> findBooksByTitel(@RequestParam String titel) {
    return bookService.searchBooksByTitel(titel);
  }

  @DeleteMapping("/book/id/{id}")
  @Operation(summary = "Delete books by id of the book",
          responses = {
                  @ApiResponse(
                          description = "Deleting a book succeeded",
                          responseCode = "200",
                          content =
                          @Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = BooksResponseDto.class))),
                  @ApiResponse(
                          description = "Deleting user not found",
                          responseCode = "404",
                          content =
                          @Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = BooksResponseDto.class))),
                  @ApiResponse(
                          description = "User is not allowed to delete a book",
                          responseCode = "403",
                          content =
                          @Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = BooksResponseDto.class))),
                  @ApiResponse(
                          description = "No book with given ID exists",
                          responseCode = "500",
                          content =
                          @Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = BooksResponseDto.class)))
          })
  public ResponseEntity<BooksResponseDto> deleteBooksById(@PathVariable Integer id, @RequestParam String actor) {
    String returnMessage = bookService.deleteById(id, actor);
    return ResponseEntity.status(HttpStatus.OK).body(new BooksResponseDto(bookService.getAllBooks(), returnMessage));
  }

  @DeleteMapping("/book/titel")
  @Operation(summary = "Delete books by the title of the book",
          responses = {
                  @ApiResponse(
                          description = "Deleting a book succeeded",
                          responseCode = "200",
                          content =
                          @Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = BooksResponseDto.class))),
                  @ApiResponse(
                          description = "Deleting user not found",
                          responseCode = "404",
                          content =
                          @Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = BooksResponseDto.class))),
                  @ApiResponse(
                          description = "User is not allowed to delete a book",
                          responseCode = "403",
                          content =
                          @Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = BooksResponseDto.class))),
                  @ApiResponse(
                          description = "One of the parameters has wrong format.",
                          responseCode = "400",
                          content =
                          @Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = BooksResponseDto.class))),
                  @ApiResponse(
                          description = "Book is assigned to at least one user",
                          responseCode = "500",
                          content =
                          @Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = BooksResponseDto.class))),
          })
  public String deleteBooksByTitel(@RequestParam String titel, @RequestParam String actor) {
    return bookService.deleteByTitel(titel, actor);
  }

  @DeleteMapping("/books")
  @Operation(summary = "Delete all books",
          responses =
              @ApiResponse(
                      description = "Deleting all books succeeded",
                      responseCode = "200",
                      content =
                      @Content(
                              mediaType = "application/json",
                              schema = @Schema(implementation = BooksResponseDto.class))))
  public ResponseEntity<BooksResponseDto> deleteAll(@RequestParam String actor) {
    String returnMessage = bookService.deleteBooks(actor);
    return ResponseEntity.status(HttpStatus.OK).body(new BooksResponseDto(bookService.getAllBooks(), returnMessage));
  }
}
