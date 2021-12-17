package project.logManager.controller;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.logManager.service.model.UserService;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    public void addUser(@RequestParam String actor,
                        @RequestParam String name,
                        @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate geburtsdatum,
                        @RequestParam double gewicht,
                        @RequestParam double groesse,
                        @RequestParam String lieblingsfarbe) {

        try {
            userService.addUser(actor, name, geburtsdatum, gewicht, groesse, lieblingsfarbe);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/user/id")
    public void findUserByID (@RequestParam final Integer id) {
        try {
            userService.findUserById(id);

        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @DeleteMapping("/user/delete/{id}")
    public void deleteUserByID (@PathVariable final Integer id) {
        try {
            userService.deleteById(id);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}