package project.logManager.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import project.logManager.model.entity.User;
import project.logManager.service.model.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor(onConstructor_ = {@Autowired})
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    public String addUser(@RequestParam String actor,
                          @RequestParam String name,
                          @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate birthdate,
                          @RequestParam double weight,
                          @RequestParam double height,
                          @RequestParam String favouriteColor) {
        return String.format("User %s wurde erstellt. " +
                        userService.addUser(actor, name, birthdate, weight, height, favouriteColor), name);
    }

    @GetMapping("/users")
    public List<User> findUsers() {
        return userService.findUserList();
    }

    @GetMapping("/user/id")
    public Optional<User> findUserByID(@RequestParam final Integer id) {
        return userService.findUserById(id);
    }

    @DeleteMapping("/user/delete/{id}")
    public String deleteUserByID(@PathVariable final Integer id,
                                 @RequestParam final String actor) {
        return userService.deleteById(id, actor);
    }

    @DeleteMapping("/user/delete/name/{name}")
    public String deleteUserByName(@PathVariable final String name,
                                   @RequestParam final String actor) {
        return userService.deleteByName(name, actor);
    }

    @DeleteMapping("/user/delete")
    public String deleteAll() {
        return userService.deleteAll();
    }
}
