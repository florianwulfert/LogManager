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
                        @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate geburtsdatum,
                        @RequestParam double gewicht,
                        @RequestParam double groesse,
                        @RequestParam String lieblingsfarbe) {
        try {
            userService.addUser(actor, name, geburtsdatum, gewicht, groesse, lieblingsfarbe);
            return String.format("User %s erstellt!", name);
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @GetMapping("/users")
    public List<User> findUsers() {
        try {
            return userService.findUserList();
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/user/id")
    public Optional<User> findUserByID (@RequestParam final Integer id) {
        try {
            return userService.findUserById(id);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @DeleteMapping("/user/delete/{id}")
    public String deleteUserByID (@PathVariable final Integer id,
        @RequestParam final String actor) {
        try {
            User user = userService.findUserByName(actor);
            User deletedUser = userService.deleteById(id, user);
            return String.format("User %s wurde gelöscht!", deletedUser.getName());
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @GetMapping("/user/bmi")
    public String findUserAndCalculateBMI (@RequestParam final String userName) {
        try {
            return userService.findUserAndCalculateBMI(userName);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}