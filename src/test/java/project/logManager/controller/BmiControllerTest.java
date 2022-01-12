package project.logManager.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.exception.UserNotFoundException;
import project.logManager.model.entity.User;
import project.logManager.service.model.BmiService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)

class BmiControllerTest {

    @InjectMocks
    BmiController systemUnderTest;

    @Mock
    BmiService bmiService;

    @Test
    void testGetBmi() {
        systemUnderTest.getBmi(LocalDate.of(2000,12,12), 90.0, 1.8);
        Mockito.verify(bmiService).getBmiMessage
                (LocalDate.of(2000,12,12), 90.0, 1.8);
    }

    @Test
    void testGetBmiThrowsException() {
        List<User> users = addTestUser();
        Mockito.when(bmiService.getBmiMessage(Mockito.any(),Mockito.any(),
                Mockito.any())).thenThrow(RuntimeException.class);
        Assertions.assertThrows(RuntimeException.class, () ->
                systemUnderTest.getBmi(users.get(0).getGeburtsdatum(), 78.0, 1.8));
        Mockito.verify(bmiService).getBmiMessage(Mockito.any(),Mockito.any(),
                Mockito.any());
    }

    @Test
    void testFindUserAndCalculateBMI() {
        List<User> users = addTestUser();
        systemUnderTest.findUserAndCalculateBMI(users.get(0).getName());
        Mockito.verify(bmiService).findUserAndCalculateBMI(users.get(0).getName());
    }

    @Test
    void testFindUserAndCalculateBmiThrowsException() {
        Mockito.when(bmiService.findUserAndCalculateBMI("Hans")).thenThrow(UserNotFoundException.class);
        Assertions.assertNull(systemUnderTest.findUserAndCalculateBMI("Hans"));
        Mockito.verify(bmiService).findUserAndCalculateBMI(Mockito.anyString());
    }

    private List<User> addTestUser() {
        List<User> users = new ArrayList<>();
        users.add(User.builder()
                .id(1)
                .name("Peter")
                .geburtsdatum(LocalDate.of(1988, 12, 12))
                .gewicht(90.0)
                .groesse(1.85)
                .lieblingsfarbe("gelb")
                .bmi(26.29)
                .build());

        users.add(User.builder()
                .id(2)
                .name("Florian")
                .geburtsdatum(LocalDate.of(1988, 12, 12))
                .gewicht(70.0)
                .groesse(1.85)
                .lieblingsfarbe("gelb")
                .bmi(20.45)
                .build());
        return users;
    }

}