package project.logManager.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import project.logManager.model.entity.User;
import project.logManager.model.respository.UserRepository;
import project.logManager.service.model.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor(onConstructor_ = {@Autowired})
@RestController
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

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
            User deletedUser = userService.deleteById(id, actor);
            return String.format("User %s wurde gelöscht!", deletedUser.getName());
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @DeleteMapping("/user/delete/name/{name}")
    public String deleteUserByName (@PathVariable final String name,
        @RequestParam final String actor) {
        try {
            User deletedUser = userService.deleteByName(name, actor);
            return String.format("User %s wurde gelöscht!", deletedUser.getName());
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @GetMapping("/user/findBmi")
    public Double findUserAndCalculateBMI (@RequestParam final String userName,
                                           @RequestParam final Double gewicht,
                                           @RequestParam final Double groesse) {
        try {
            return userService.findUserAndCalculateBMI(userName, gewicht, groesse);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/user/bmi")
    public String berechneBMI(@RequestParam Double gewicht,
                              @RequestParam Double groesse) {
        try {
            userService.berechneBMI(gewicht, groesse);
            return "BMI wurde berechnet";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/user/bmiWithMessage")
    public String berechneBMIwithMessage(@RequestParam LocalDate geburtsDatum,
                                         @RequestParam Double gewicht,
                                         @RequestParam Double groesse) {
        try {
            return userService.berechneBmiWithMessage(geburtsDatum, gewicht,groesse);
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}