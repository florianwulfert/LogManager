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
import project.logManager.common.dto.UserRequestDto;
import project.logManager.common.dto.UserResponseDto;
import project.logManager.model.entity.User;
import project.logManager.service.model.UserService;

@AllArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@CrossOrigin
@Tag(name="User")
public class UserController {

  private final UserService userService;

  @PostMapping("/user")
  public String addUser(@RequestBody UserRequestDto allParameters) {
    return String.format(
        "User %s was created. " + userService.addUser(allParameters), allParameters.name);
  }

  @GetMapping("/users")
  public UserResponseDto findUsers() {
    return new UserResponseDto(userService.findUserList());
  }

  @GetMapping("/user/id")
  public Optional<User> findUserByID(@RequestParam final Integer id) {
    return userService.findUserById(id);
  }

  @DeleteMapping("/user/delete/{id}")
  public String deleteUserByID(@PathVariable final Integer id, @RequestParam final String actor) {
    return userService.deleteById(id, actor);
  }

  @DeleteMapping("/user/delete/name/{name}")
  public String deleteUserByName(
      @PathVariable final String name, @RequestParam final String actor) {
    return userService.deleteByName(name, actor);
  }

  @DeleteMapping("/user/delete")
  public String deleteAll() {
    return userService.deleteAll();
  }
}
