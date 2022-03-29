package project.logManager.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.logManager.common.dto.UserRequestDto;
import project.logManager.common.dto.UserResponseDto;
import project.logManager.model.entity.User;
import project.logManager.service.model.UserService;

import java.util.Optional;

@AllArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@CrossOrigin
@Tag(name="User")
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

  @DeleteMapping("/user/delete/{id}")
  public UserResponseDto deleteUserByID(@PathVariable final Integer id, @RequestParam final String actor) {
    userService.deleteById(id, actor);
    return new UserResponseDto(userService.findUserList(), null);
  }

  @DeleteMapping("/user/delete/name/{name}")
  public String deleteUserByName(
      @PathVariable final String name, @RequestParam final String actor) {
    return userService.deleteByName(name, actor);
  }

  @DeleteMapping("/user/delete")
  public UserResponseDto deleteAll() {
    String returnMessage = userService.deleteAll();
    return new UserResponseDto(userService.findUserList(), returnMessage);
  }
}
