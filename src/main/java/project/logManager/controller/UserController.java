package project.logManager.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.logManager.common.dto.user.UserRequestDto;
import project.logManager.common.dto.user.UserResponseDto;
import project.logManager.model.entity.User;
import project.logManager.service.model.UserService;

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
