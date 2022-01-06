package project.logManager.controller;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.logManager.service.model.BmiService;
import project.logManager.service.model.UserService;

import java.time.LocalDate;

@AllArgsConstructor(onConstructor_ = {@Autowired})
@RestController
public class BmiController {

    private final BmiService bmiService;
    private final UserService userService;

    @GetMapping("/bmiMessage")
    public String getBmiMessage(
            @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate geburtsdatum,
            @RequestParam double gewicht,
            @RequestParam double groesse) {
        try {
            return bmiService.getBmiMessage(geburtsdatum, gewicht, groesse);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/bmi")
    public Double berechneBmi(@RequestParam double gewicht,
                              @RequestParam double groesse) {
        try {
            return bmiService.berechneBMI(gewicht, groesse);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/user/findBmi")
    public Double findUserAndCalculateBMI (@RequestParam final String userName) {
        try {
            return bmiService.findUserAndCalculateBMI(userName);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
