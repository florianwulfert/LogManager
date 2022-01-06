package project.logManager.controller;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.logManager.service.model.BmiService;

import java.time.LocalDate;

@AllArgsConstructor(onConstructor_ = {@Autowired})
@RestController
public class BmiController {

    private final BmiService bmiService;

    @GetMapping("/bmi")
    public String getBmi(
            @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate geburtsdatum,
            @RequestParam double gewicht,
            @RequestParam double groesse) {
        try {
            return bmiService.getBmiMessage(geburtsdatum, gewicht, groesse);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/bmi/{user}")
    public Double findUserAndCalculateBMI (@PathVariable final String user) {
        try {
            return bmiService.findUserAndCalculateBMI(user);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
