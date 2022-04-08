package project.userFeaturePortal.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.userFeaturePortal.common.dto.user.FindUserResponseDto;
import project.userFeaturePortal.common.dto.user.UserRequestDto;
import project.userFeaturePortal.common.dto.user.UserResponseDto;
import project.userFeaturePortal.model.entity.User;
import project.userFeaturePortal.service.model.UserService;

import java.util.Optional;

@AllArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@CrossOrigin
@Tag(name = "User")
public class UserController {

  private final UserService userService;

  @PostMapping("/user")
  public UserResponseDto addUser(@RequestBody UserRequestDto allParameters) {
    String returnMessage = userService.addUser(allParameters);
    return new UserResponseDto(userService.findUserList(), returnMessage);
  }

  @GetMapping("/users")
  public UserResponseDto findUsers() {
    return new UserResponseDto(userService.findUserList(), null);
  }

  @GetMapping("/user/id")
  public Optional<User> findUserByID(@RequestParam final Integer id) {
    return userService.findUserById(id);
  }

  @GetMapping("/user")
  public FindUserResponseDto findUserByName(@RequestParam final String name) {
    return new FindUserResponseDto(userService.findUserByName(name));
  }

  @DeleteMapping("/user/delete/{id}")
  public UserResponseDto deleteUserByID(@PathVariable final Integer id, @RequestParam final String actor) {
    userService.deleteById(id, actor);
    return new UserResponseDto(userService.findUserList(), null);
  }

  @DeleteMapping("/user/delete/name/{name}")
  public UserResponseDto deleteUserByName(
      @PathVariable final String name, @RequestParam final String actor) {
    String returnMessage = userService.deleteByName(name, actor);
    return new UserResponseDto(userService.findUserList(), returnMessage);
  }

  @DeleteMapping("/user/delete")
  public UserResponseDto deleteAll() {
    String returnMessage = userService.deleteAll();
    return new UserResponseDto(userService.findUserList(), returnMessage);
  }
}
