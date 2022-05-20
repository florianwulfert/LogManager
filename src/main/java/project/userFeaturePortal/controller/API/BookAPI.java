package project.userFeaturePortal.controller.API;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.userFeaturePortal.common.dto.books.BookRequestDto;
import project.userFeaturePortal.common.dto.books.BooksResponseDto;
import project.userFeaturePortal.common.dto.books.FindBookResponseDto;

public interface BookAPI {

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
                                                    "{\"result\":[{\"id\":1,\"titel\":\"TestBook1\",\"erscheinungsjahr\":1999}"
                                                            + ",{\"id\":2,\"titel\":\"TestBook2\",\"erscheinungsjahr\":1985}], \"returnMessage\":null}",
                                            allOf = BooksResponseDto.class)))
            })
    ResponseEntity<BooksResponseDto> getAllBooks();

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
    ResponseEntity<BooksResponseDto> addBook(@RequestBody BookRequestDto parameters);

    @GetMapping("/book")
    @Operation(
            summary = "Found book by titel of the book",
            responses = {
                    @ApiResponse(
                            description = "Book found",
                            responseCode = "200",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema =
                                    @Schema(
                                            example =
                                                    "{\"id\":5,\"titel\":\"TestBook\",\"erscheinungsjahr\":2002},"))),
            })
    ResponseEntity<FindBookResponseDto> findBooksByTitel(@RequestParam String titel);

    @PostMapping("/book/update")
    @Operation(
            summary = "Update manually an existing Book",
            responses = {
                    @ApiResponse(
                            description = "Updating a book succeeded",
                            responseCode = "200",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema =
                                    @Schema(
                                            example =
                                                    "{\"result\":[{\"id\": 318,\"titel\": \"TestBook\","
                                                            + "\"erscheinungsjahr\": 2000}],\"returnMessage\": \"Book TestBook was updated.\"}",
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
    ResponseEntity<BooksResponseDto> updateBook(@RequestBody BookRequestDto allParameters);

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
    ResponseEntity<BooksResponseDto> deleteBooksById(@PathVariable Integer id, @RequestParam String actor);

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
    ResponseEntity<BooksResponseDto> deleteBooksByTitel(@RequestParam String titel, @RequestParam String actor);

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
    ResponseEntity<BooksResponseDto> deleteAll(@RequestParam String actor);
}
