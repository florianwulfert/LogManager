package project.logManager.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.logManager.service.model.UserService;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
public class BMIController {

    private final UserService userService;

    @PostMapping("/bmi")
    public String berechneBMI(@RequestParam LocalDate geburtsDatum,
                              @RequestParam Double gewicht,
                              @RequestParam Double groesse) {
        try {
            userService.berechneBMI(geburtsDatum, groesse, gewicht);
        return "BMI wurde berechnet!";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }


}
