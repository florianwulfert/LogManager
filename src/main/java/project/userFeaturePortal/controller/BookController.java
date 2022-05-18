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

  @Autowired private final BookService bookService;

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
                    schema =
                        @Schema(
                            example =
                                "{\"result\":[{\"id\":1,\"name\":\"Petra\",\"birthdate\":\"1999-12-13\","
                                    + "\"weight\":65.0,\"height\":1.6,\"favouriteBookTitel\":null,\"bmi\":25.39}"
                                    + ",{\"id\":2,\"name\":\"Torsten\",\"birthdate\":\"1985-12-05\",\"weight\":61.3,\"height\":1.83,"
                                    + "\"favouriteBookTitel\":null,\"bmi\":18.3}]}",
                            allOf = BooksResponseDto.class)))
      })
  public ResponseEntity<BooksResponseDto> getAllBooks() {
    return ResponseEntity.status(HttpStatus.OK)
        .body(new BooksResponseDto(bookService.getAllBooks(), null));
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
                    schema =
                        @Schema(
                            example =
                                "{\"result\":[{\"id\": 318,\"titel\": \"TestBook\","
                                    + "\"erscheinungsjahr\": 0}],\"returnMessage\": \"Book TestBook was created.\"}",
                            allOf = BooksResponseDto.class))),
        @ApiResponse(
            description = "User is not allowed to add a book",
            responseCode = "403",
            content =
                @Content(
                    mediaType = "text/plain",
                    schema = @Schema(example = "User is not allowed to execute this operation."))),
        @ApiResponse(
            description = "One of the parameters has wrong format.",
            responseCode = "400",
            content =
                @Content(
                    mediaType = "text/plain",
                    schema = @Schema(example = "One of the parameters has wrong format.")))
      })
  public ResponseEntity<BooksResponseDto> addBook(@RequestBody BookRequestDto parameters) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            new BooksResponseDto(
                bookService.addBook(
                    parameters.erscheinungsjahr, parameters.titel, parameters.actor),
                String.format(InfoMessages.BOOK_CREATED, parameters.titel)));
  }

  @PostMapping("/bookUpdate")
  @Operation(
          summary = "Update manually an existing Book",
          responses = {
                  @ApiResponse(
                          description = "Updating a book succeeded",
                          responseCode = "201",
                          content =
                          @Content(
                                  mediaType = "application/json",
                                  schema =
                                  @Schema(
                                          example =
                                                  "{\"result\":[{\"id\": 318,\"titel\": \"TestBook\","
                                                          + "\"erscheinungsjahr\": 0}],\"returnMessage\": \"Book TestBook was updated.\"}",
                                          allOf = BooksResponseDto.class))),
                  @ApiResponse(
                          description = "User is not allowed to execute this operation.",
                          responseCode = "403",
                          content =
                          @Content(
                                  mediaType = "text/plain",
                                  schema = @Schema(example = "User is not allowed to execute this operation."))),
                  @ApiResponse(
                          description = "One of the parameters has wrong format.",
                          responseCode = "400",
                          content =
                          @Content(
                                  mediaType = "text/plain",
                                  schema = @Schema(example = "One of the parameters has wrong format.")))
          })
  public ResponseEntity<BooksResponseDto> updateUser(@RequestBody BookRequestDto allParameters) {
    String returnMessage = bookService.updateUser(allParameters);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new BooksResponseDto(bookService.getAllBooks(), returnMessage));
  }

  @GetMapping("/book")
  @Operation(
      summary = "Found books by titel of the book",
      responses = {
        @ApiResponse(
            description = "Book(s) found",
            responseCode = "200",
            content =
                @Content(
                    mediaType = "application/json",
                    schema =
                        @Schema(
                            example =
                                "{\"id\":5,\"titel\":\"TestBook\",\"erscheinungsjahr\":2002},"))),
      })
  public List<Book> findBooksByTitel(@RequestParam String titel) {
    return bookService.searchBooksByTitel(titel);
  }

  @DeleteMapping("/book/id/{id}")
  @Operation(
      summary = "Delete books by id of the book",
      responses = {
        @ApiResponse(
            description = "Deleting a book succeeded",
            responseCode = "200",
            content =
                @Content(
                    mediaType = "application/json",
                    schema =
                        @Schema(
                            example =
                                "{\"result\":[{\"id\":2,\"titel\":\"petra\",\"erscheinungsjahr\":1989},"
                                    + "{\"id\":7,\"titel\":\"paul\",\"erscheinungsjahr\":2008}],"
                                    + "\"returnMessage\":\"Book with the ID 1 was deleted.\"}",
                            allOf = BooksResponseDto.class))),
        @ApiResponse(
            description = "User is not allowed to delete a book",
            responseCode = "403",
            content =
                @Content(
                    mediaType = "text/plain",
                    schema = @Schema(example = "User is not allowed to execute this operation."))),
        @ApiResponse(
            description = "No book with given ID exists",
            responseCode = "500",
            content =
                @Content(
                    mediaType = "text/plain",
                    schema =
                        @Schema(
                            example =
                                "No class project.userFeaturePortal.model.entity.Book entity with id 1 exists!")))
      })
  public ResponseEntity<BooksResponseDto> deleteBooksById(
      @PathVariable Integer id, @RequestParam String actor) {
    String returnMessage = bookService.deleteById(id, actor);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new BooksResponseDto(bookService.getAllBooks(), returnMessage));
  }

  @DeleteMapping("/book/titel")
  @Operation(
      summary = "Delete books by the title of the book",
      responses = {
        @ApiResponse(
            description = "Deleting a book succeeded",
            responseCode = "200",
            content =
                @Content(
                    mediaType = "text/plain",
                    schema = @Schema(example = "Book with the title TestBook was deleted."))),
        @ApiResponse(
            description = "User is not allowed to delete a book",
            responseCode = "403",
            content =
                @Content(
                    mediaType = "text/plain",
                    schema = @Schema(example = "User is not allowed to execute this operation."))),
        @ApiResponse(
            description = "One of the parameters has wrong format.",
            responseCode = "400",
            content =
                @Content(
                    mediaType = "text/plain",
                    schema = @Schema(example = "One of the parameters has wrong format."))),
        @ApiResponse(
            description = "Book is assigned to at least one user",
            responseCode = "500",
            content =
                @Content(
                    mediaType = "text/plain",
                    schema =
                        @Schema(example = "Book with ID 1 is assigned to at least one user."))),
      })
  public ResponseEntity<BooksResponseDto> deleteBooksByTitel(
      @RequestParam String titel, @RequestParam String actor) {
    String returnMessage = bookService.deleteByTitel(titel, actor);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new BooksResponseDto(bookService.getAllBooks(), returnMessage));
  }

  @DeleteMapping("/books")
  @Operation(
      summary = "Delete all books",
      responses =
          @ApiResponse(
              description = "Deleting all books succeeded",
              responseCode = "200",
              content =
                  @Content(
                      mediaType = "application/json",
                      schema =
                          @Schema(
                              example =
                                  "{\"result\":[],\"returnMessage\":\"All BOOKS were deleted from database!\"}",
                              allOf = BooksResponseDto.class))))
  public ResponseEntity<BooksResponseDto> deleteAll(@RequestParam String actor) {
    String returnMessage = bookService.deleteBooks(actor);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new BooksResponseDto(bookService.getAllBooks(), returnMessage));
  }
}
