package project.userFeaturePortal.controller.API;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.userFeaturePortal.common.dto.user.*;
import project.userFeaturePortal.model.entity.User;

import java.util.Optional;

public interface UserAPI {
  @PostMapping("/user")
  ResponseEntity<UserResponseDto> addUser(@RequestBody UserRequestDto allParameters);

  @PostMapping("/user/favouriteBook")
  String addFavouriteBookToUser(@RequestParam final String bookTitel, @RequestParam final String actor);

  @PostMapping("/user/favouriteBook/delete")
  String deleteFavouriteBook(@RequestParam String name);

  @PostMapping("/user/update")
  ResponseEntity<UserResponseDto> updateUser(@RequestBody UserRequestDto allParameters);

  @GetMapping("/users")
  ResponseEntity<UserResponseDto> findUsers();

  @GetMapping("/user/id")
  Optional<User> findUserByID(@RequestParam final Integer id);

  @GetMapping("/user")
  ResponseEntity<FindUserResponseDto> findUserByName(@RequestParam final String name);

  @GetMapping("/user/validate")
  ResponseEntity<ValidateUserResponseDto> validateUserByName(@RequestParam final String name);

  @GetMapping("/user/favouriteBook")
  ResponseEntity<GetFavouriteBookResponseDto> getFavouriteBook(@RequestParam final String name);

  @DeleteMapping("/user/id/{id}")
  ResponseEntity<UserResponseDto> deleteUserByID(@PathVariable final Integer id, @RequestParam final String actor);

  @DeleteMapping("/user/name/{name}")
  ResponseEntity<UserResponseDto> deleteUserByName(@PathVariable final String name, @RequestParam final String actor);

  @DeleteMapping("/users")
  ResponseEntity<UserResponseDto> deleteAll();
}
