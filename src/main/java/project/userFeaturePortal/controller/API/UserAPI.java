package project.userFeaturePortal.controller.API;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.userFeaturePortal.common.dto.user.*;
import project.userFeaturePortal.model.entity.User;

import java.util.Optional;

public interface UserAPI {

    @RequestMapping("/login")
    boolean login(@RequestBody User user);

    @PostMapping("/user")
    @Operation(
            summary = "Add  an user manually",
            responses = {
                    @ApiResponse(
                            description = "Adding an user succeeded",
                            responseCode = "201",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema =
                                    @Schema(
                                            example =
                                                    "{\"result\":[{\"id\":1,\"name\":\"Petra\",\"birthdate\":\"1999-12-13\","
                                                            + "\"weight\":65.0,\"height\":1.6,\"favouriteBookTitel\":null,\"bmi\":25.39}"
                                                            + ",{\"id\":2,\"name\":\"Torsten\",\"birthdate\":\"1985-12-05\",\"weight\":61.3,\"height\":1.83,"
                                                            + "\"favouriteBookTitel\":null,\"bmi\":18.3},"
                                                            + "{\"id\":3,\"name\":\"Hans\",\"birthdate\":\"1993-02-03\",\"weight\":75.7,\"height\":1.85,"
                                                            + "\"favouriteBookTitel\":null,\"bmi\":22.11}," +
                                                            "{\"id\":4,\"name\":\"Hugo\",\"birthdate\":\"1999-12-13\",\"weight\":78.0,\"height\":1.8,\"favouriteBookTitel\":null,\"bmi\":24.07}]," +
                                                            " \"returnMessage\":\"User Florian was created.\"}",
                                            allOf = UserResponseDto.class))),
                    @ApiResponse(
                            description = "User is not allowed to add another user",
                            responseCode = "403",
                            content =
                            @Content(
                                    mediaType = "text/plain",
                                    schema =
                                    @Schema(example = "User Hans has no authority to create user."
                                    )
                            )
                    ),
                    @ApiResponse(
                            description = "User has to create himself first.",
                            responseCode = "500",
                            content =
                            @Content(
                                    mediaType = "text/plain",
                                    schema =
                                    @Schema(example = "User cannot be created because there are no users in the database yet." +
                                            "First user has to create himself!"
                                    )
                            )
                    ),
                    @ApiResponse(
                            description = "Parameter wrong or missing or user already exists.",
                            responseCode = "400",
                            content =
                            @Content(
                                    mediaType = "text/plain",
                                    schema =
                                    @Schema(example = "User Hans already exists."
                                    )
                            )
                    )
            })
    ResponseEntity<UserResponseDto> addUser(@RequestBody UserRequestDto allParameters);

    @PostMapping("/user/favouriteBook")
    @Operation(
            summary = "Add a favourite book to an user",
            responses = {
                    @ApiResponse(
                            description = "Adding a favourite book succeeded",
                            responseCode = "200",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema =
                                    @Schema(
                                            example =
                                                    "{\"favouriteBook\":\"TestBook\",\"returnMessage\":\"Book TestBook added to user Petra.\"}",
                                            allOf = UserResponseDto.class))),
                    @ApiResponse(
                            description = "Actor was not found so he is not allowed to add favourite books",
                            responseCode = "403",
                            content =
                            @Content(
                                    mediaType = "text/plain",
                                    schema =
                                    @Schema(
                                            example = "User is not allowed to execute this operation."))),
                    @ApiResponse(
                            description = "Book not found",
                            responseCode = "500",
                            content =
                            @Content(
                                    mediaType = "text/plain",
                                    schema =
                                    @Schema(
                                            example = "Book with titel TestBook not found!"))),

            })
    ResponseEntity<GetFavouriteBookResponseDto> addFavouriteBookToUser(@RequestParam final String bookTitel, @RequestParam final String actor);

    @PostMapping("/user/favouriteBook/delete")
    @Operation(
            summary = "Delete a favourite book from an user",
            responses = {
                    @ApiResponse(
                            description = "Deleting a favourite book succeeded",
                            responseCode = "200",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema =
                                    @Schema(
                                            example =
                                                    "{\"favouriteBook\":\"\",\"returnMessage\":\"User Flo does not have a favourite book anymore.\"}",
                                            allOf = UserResponseDto.class))),
                    @ApiResponse(
                            description = "User not found",
                            responseCode = "404",
                            content =
                            @Content(
                                    mediaType = "text/plain",
                                    schema =
                                    @Schema(
                                            example = "User named Peter not found!")))
            })
    ResponseEntity<GetFavouriteBookResponseDto> deleteFavouriteBook(@RequestParam String name);

    @PostMapping("/user/update")
    @Operation(
            summary = "Updating users information",
            responses = {
                    @ApiResponse(
                            description = "Updating an user book succeeded",
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
                                                            + "\"favouriteBookTitel\":null,\"bmi\":18.3},"
                                                            + "{\"id\":3,\"name\":\"Hans\",\"birthdate\":\"1993-02-03\",\"weight\":75.7,\"height\":1.85,"
                                                            + "\"favouriteBookTitel\":null,\"bmi\":22.11}," +
                                                            "{\"id\":4,\"name\":\"Hugo\",\"birthdate\":\"1999-12-13\",\"weight\":78.0,\"height\":1.8,\"favouriteBookTitel\":null,\"bmi\":24.07}]," +
                                                            " \"returnMessage\":\"User Florian updated.\"}",
                                            allOf = UserResponseDto.class))),
                    @ApiResponse(
                            description = "User not found",
                            responseCode = "404",
                            content =
                            @Content(
                                    mediaType = "text/plain",
                                    schema =
                                    @Schema(
                                            example = "User named Peter not found!"))),
                    @ApiResponse(
                            description = "Parameter wrong or missing or user already exists.",
                            responseCode = "400",
                            content =
                            @Content(
                                    mediaType = "text/plain",
                                    schema =
                                    @Schema(example = "User Hans already exists."
                                    )
                            )
                    )
            })
    ResponseEntity<UserResponseDto> updateUser(@RequestBody UserRequestDto allParameters);

    @GetMapping("/users")
    @Operation(
            summary = "Get list of all users",
            responses = {
                    @ApiResponse(
                            description = "Get list of all users succeeded",
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
                                                            + "\"favouriteBookTitel\":null,\"bmi\":18.3},"
                                                            + "{\"id\":3,\"name\":\"Hans\",\"birthdate\":\"1993-02-03\",\"weight\":75.7,\"height\":1.85,"
                                                            + "\"favouriteBookTitel\":null,\"bmi\":22.11}," +
                                                            "{\"id\":4,\"name\":\"Hugo\",\"birthdate\":\"1999-12-13\",\"weight\":78.0,\"height\":1.8,\"favouriteBookTitel\":null,\"bmi\":24.07}]," +
                                                            " \"returnMessage\":null}",
                                            allOf = UserResponseDto.class))),
            })
    ResponseEntity<UserResponseDto> findUsers();

    @GetMapping("/user/id")
    @Operation(
            summary = "Find user by his id",
            responses = {
                    @ApiResponse(
                            description = "User successfully found by id",
                            responseCode = "200",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema =
                                    @Schema(
                                            example =
                                                    "{\n" +
                                                            "  \"id\": 1,\n" +
                                                            "  \"name\": \"Flo\",\n" +
                                                            "  \"birthdate\": \"2005-12-12\",\n" +
                                                            "  \"weight\": 79,\n" +
                                                            "  \"height\": 1.77,\n" +
                                                            "  \"favouriteBook\": null,\n" +
                                                            "  \"bmi\": 25.21,\n" +
                                                            "  \"bmiMessage\": \"User is too young for an exact definition of the BMI.\"\n" +
                                                            "}",
                                            allOf = UserResponseDto.class))),
            })
    Optional<User> findUserByID(@RequestParam final Integer id);

    @GetMapping("/user")
    @Operation(
            summary = "Find user by name",
            responses = {
                    @ApiResponse(
                            description = "User successfully found by name",
                            responseCode = "200",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema =
                                    @Schema(
                                            example =
                                                    "{\n" +
                                                            "  \"user\": {\n" +
                                                            "    \"id\": 3,\n" +
                                                            "    \"name\": \"Florian\",\n" +
                                                            "    \"birthdate\": \"2002-03-08\",\n" +
                                                            "    \"weight\": 80,\n" +
                                                            "    \"height\": 1.77,\n" +
                                                            "    \"favouriteBookTitel\": null,\n" +
                                                            "    \"bmi\": 25.53\n" +
                                                            "  }\n" +
                                                            "}",
                                            allOf = UserResponseDto.class))),
            })
    ResponseEntity<FindUserResponseDto> findUserByName(@RequestParam final String name);

    @GetMapping("/user/validate")
    @Operation(
            summary = "Get boolean value for existing user",
            responses = {
                    @ApiResponse(
                            description = "User found or not found",
                            responseCode = "200",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema =
                                    @Schema(
                                            example = "{\"foundUser\":true}",
                                            allOf = ValidateUserResponseDto.class))),
            })
    ResponseEntity<ValidateUserResponseDto> validateUserByName(@RequestParam final String name);

    @GetMapping("/user/favouriteBook")
    @Operation(
            summary = "Get favourite book of an user",
            responses = {
                    @ApiResponse(
                            description = "Getting favourite book succeeded",
                            responseCode = "200",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema =
                                    @Schema(
                                            example =
                                                    "{\"favouriteBook\":\"TestBook\",\"returnMessage\":null}",
                                            allOf = GetFavouriteBookResponseDto.class))),
                    @ApiResponse(
                            description = "User not found",
                            responseCode = "404",
                            content =
                            @Content(
                                    mediaType = "text/plain",
                                    schema =
                                    @Schema(
                                            example = "User named Peter not found!")))
            })
    ResponseEntity<GetFavouriteBookResponseDto> getFavouriteBook(@RequestParam final String name);

    @DeleteMapping("/user/id/{id}")
    @Operation(
            summary = "Delete user by id",
            responses = {
                    @ApiResponse(
                            description = "User successfully deleted",
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
                                                            + "\"favouriteBookTitel\":null,\"bmi\":18.3},"
                                                            + "{\"id\":3,\"name\":\"Hans\",\"birthdate\":\"1993-02-03\",\"weight\":75.7,\"height\":1.85,"
                                                            + "\"favouriteBookTitel\":null,\"bmi\":22.11}," +
                                                            "{\"id\":4,\"name\":\"Hugo\",\"birthdate\":\"1999-12-13\",\"weight\":78.0,\"height\":1.8,\"favouriteBookTitel\":null,\"bmi\":24.07}]," +
                                                            " \"returnMessage\":null}",
                                            allOf = UserResponseDto.class))),
                    @ApiResponse(
                            description = "Actor not found",
                            responseCode = "403",
                            content =
                            @Content(
                                    mediaType = "text/plain",
                                    schema =
                                    @Schema(
                                            example = "User Peter has no authority to delete user."))),
                    @ApiResponse(
                            description = "User to delete not found, User to delete referenced anywhere, User wants to delete himself",
                            responseCode = "500",
                            content =
                            @Content(
                                    mediaType = "text/plain",
                                    schema =
                                    @Schema(
                                            example = "User with the ID 8 not found.")))
            })
    ResponseEntity<UserResponseDto> deleteUserByID(@PathVariable final Integer id, @RequestParam final String actor);

    @DeleteMapping("/user/name/{name}")
    @Operation(
            summary = "Delete user by name",
            responses = {
                    @ApiResponse(
                            description = "User successfully deleted",
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
                                                            + "\"favouriteBookTitel\":null,\"bmi\":18.3},"
                                                            + "{\"id\":3,\"name\":\"Hans\",\"birthdate\":\"1993-02-03\",\"weight\":75.7,\"height\":1.85,"
                                                            + "\"favouriteBookTitel\":null,\"bmi\":22.11}," +
                                                            "{\"id\":4,\"name\":\"Hugo\",\"birthdate\":\"1999-12-13\",\"weight\":78.0,\"height\":1.8,\"favouriteBookTitel\":null,\"bmi\":24.07}]," +
                                                            " \"returnMessage\":\"User named Florian deleted.\"}",
                                            allOf = UserResponseDto.class))),
                    @ApiResponse(
                            description = "User to delete not found",
                            responseCode = "404",
                            content =
                            @Content(
                                    mediaType = "text/plain",
                                    schema =
                                    @Schema(
                                            example = "User named Peter not found!"))),
                    @ApiResponse(
                            description = "Actor not found",
                            responseCode = "403",
                            content =
                            @Content(
                                    mediaType = "text/plain",
                                    schema =
                                    @Schema(
                                            example = "User Peter has no authority to delete user."))),
                    @ApiResponse(
                            description = "User to delete referenced anywhere, User wants to delete himself",
                            responseCode = "500",
                            content =
                            @Content(
                                    mediaType = "text/plain",
                                    schema =
                                    @Schema(
                                            example = "User Florian cannot be deleted because he is referenced in another table!")))
            })
    ResponseEntity<UserResponseDto> deleteUserByName(@PathVariable final String name, @RequestParam final String actor);

    @DeleteMapping("/users")
    @Operation(
            summary = "Delete all users",
            responses = {
                    @ApiResponse(
                            description = "Deleting of all users succeeded",
                            responseCode = "200",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema =
                                    @Schema(
                                            example =
                                                    "{\"result\":[],\"returnMessage\":\"All users were deleted from database!\"}",
                                            allOf = UserResponseDto.class))),
                    @ApiResponse(
                            description = "Users are referenced in another table",
                            responseCode = "500",
                            content =
                            @Content(
                                    mediaType = "text/plain",
                                    schema =
                                    @Schema(
                                            example =
                                                    "Users cannot be deleted because they are referenced in another table!",
                                            allOf = UserResponseDto.class))),
            })
    ResponseEntity<UserResponseDto> deleteAll();
}
