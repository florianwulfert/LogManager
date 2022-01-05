package project.logManager.controller;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.logManager.service.model.BmiService;

import java.time.LocalDate;

@AllArgsConstructor(onConstructor_ = {@Autowired})
@RestController
public class BmiController {

    private final BmiService bmiService;

    @PostMapping("/bmi")
    public String berechneBMI(
            @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate geburtsdatum,
            @RequestParam double gewicht,
            @RequestParam double groesse) {
        try {
            return bmiService.berechneBmi(geburtsdatum, gewicht, groesse);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
