package project.logManager.service.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.exception.UserNotFoundException;
import project.logManager.model.entity.User;
import project.logManager.model.repository.UserRepository;

@ExtendWith(MockitoExtension.class)

class BmiServiceTest {

    @InjectMocks
    BmiService systemUnderTest;

    @Mock
    UserRepository userRepository;

    List<User> users;

    @BeforeEach
    void init() {
        users = addTestUser();
    }

    @Test
    void testBerechneBMIWhenUserTooYoung() {
        Assertions.assertEquals("Der User ist zu jung für eine genaue Bestimmung des BMI.",
                systemUnderTest.getBmiMessage(users.get(0).getGeburtsdatum(),
                        users.get(0).getGewicht(),
                        users.get(0).getGroesse()));
    }

    @Test
    void testBerechneBMIWithNormalWeight() {
        Assertions.assertEquals("Der User hat einen BMI von 23.14 und ist somit normalgewichtig.",
                systemUnderTest.getBmiMessage(LocalDate.of(1988, 12, 12),
                        75.0, 1.80));
    }
    @Test
    void testBerechneBMIWithUnderweight() {
        Assertions.assertEquals("Der User hat einen BMI von 16.97 und ist somit untergewichtig.",
                systemUnderTest.getBmiMessage(LocalDate.of(1988, 12, 12),
                        55.0, 1.80));
    }
    @Test
    void testBerechneBMIWithOverweight() {
        Assertions.assertEquals("Der User hat einen BMI von 44.44 und ist somit übergewichtig.",
                systemUnderTest.getBmiMessage(LocalDate.of(2000, 12,12),
                        100.0, 1.50));
    }

    @Test
    void testBMIisUnexpectedValue() {
        RuntimeException ex = Assertions.assertThrows(IllegalStateException.class, () ->
                systemUnderTest.getBmiMessage(LocalDate.of(2000, 12, 12),
                        -100.0, 1.85));
        Assertions.assertEquals("BMI konnte nicht berechnet werden", ex.getMessage());
    }

    @Test
    void testBerechneBMI() {
        Assertions.assertEquals(30.86,
                systemUnderTest.berechneBMI(100.0, 1.8));
    }

    @Test
    void testFindUserAndCalculateBMI() {
        Mockito.when(userRepository.findUserByName(users.get(1).getName())).thenReturn(users.get(1));
        systemUnderTest.findUserAndCalculateBMI(users.get(1).getName());
    }

    @Test
    void testUserIsNull() {
        UserNotFoundException ex = Assertions.assertThrows(UserNotFoundException.class, () ->
                systemUnderTest.findUserAndCalculateBMI(users.get(0).getName()));
        Assertions.assertEquals("User Peter konnte nicht identifiziert werden!", ex.getMessage());
    }

    private List<User> addTestUser() {
        List<User> users = new ArrayList<>();
        users.add(User.builder()
                .id(1)
                .name("Peter")
                .geburtsdatum(LocalDate.of(2005, 12, 12))
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