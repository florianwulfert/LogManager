package project.userFeaturePortal.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.userFeaturePortal.common.dto.user.FindUserResponseDto;
import project.userFeaturePortal.common.dto.user.UserRequestDto;
import project.userFeaturePortal.common.dto.user.UserResponseDto;
import project.userFeaturePortal.model.entity.User;
import project.userFeaturePortal.service.model.UserService;

import java.util.Optional;

@AllArgsConstructor(onConstructor_ = { @Autowired })
@RestController
@CrossOrigin
@Tag(name = "User")
public class UserController {

  private final UserService userService;

  @PostMapping("/user")
  public ResponseEntity<UserResponseDto> addUser(@RequestBody UserRequestDto allParameters) {
    String returnMessage = userService.addUser(allParameters);
    return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponseDto(userService.findUserList(), returnMessage));
  }

  @PostMapping("/user/favouriteBook")
  public String addFavouriteBookToUser(
      @RequestParam final String bookTitel,
      @RequestParam final String actor) {
    return userService.addFavouriteBookToUser(bookTitel, actor);
  }

  @GetMapping("/users")
  public ResponseEntity<UserResponseDto> findUsers() {
    return ResponseEntity.status(HttpStatus.OK).body(new UserResponseDto(userService.findUserList(), null));
  }

  @GetMapping("/user/id")
  public Optional<User> findUserByID(@RequestParam final Integer id) {
    return userService.findUserById(id);
  }

  @GetMapping("/user")
  public ResponseEntity<FindUserResponseDto> findUserByName(@RequestParam final String name) {
    return ResponseEntity.status(HttpStatus.OK).body(new FindUserResponseDto(userService.findUserByName(name)));
  }

  @DeleteMapping("/user/id/{id}")
  public ResponseEntity<UserResponseDto> deleteUserByID(@PathVariable final Integer id, @RequestParam final String actor) {
    userService.deleteById(id, actor);
    return ResponseEntity.status(HttpStatus.OK).body(new UserResponseDto(userService.findUserList(), null));
  }

  @DeleteMapping("/user/name/{name}")
  public ResponseEntity<UserResponseDto> deleteUserByName(
      @PathVariable final String name, @RequestParam final String actor) {
    String returnMessage = userService.deleteByName(name, actor);
    return ResponseEntity.status(HttpStatus.OK).body(new UserResponseDto(userService.findUserList(), returnMessage));
  }

  @DeleteMapping("/users")
  public ResponseEntity<UserResponseDto> deleteAll() {
    String returnMessage = userService.deleteAll();
    return ResponseEntity.status(HttpStatus.OK).body(new UserResponseDto(userService.findUserList(), returnMessage));
  }
}
