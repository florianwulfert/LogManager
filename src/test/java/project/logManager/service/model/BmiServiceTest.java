package project.logManager.service.model;

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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        Assertions.assertEquals("User is too young for an exact definition of the BMI.",
                systemUnderTest.getBmiMessage(users.get(0).getBirthdate(),
                        users.get(0).getWeight(),
                        users.get(0).getHeight()));
    }

    @Test
    void testBerechneBMIWithNormalWeight() {
        Assertions.assertEquals("User has a BMI of 23.14 and therewith he has normal weight.",
                systemUnderTest.getBmiMessage(LocalDate.of(1988, 12, 12),
                        75.0, 1.80));
    }
    @Test
    void testBerechneBMIWithUnderweight() {
        Assertions.assertEquals("User has a BMI of 16.97 and therewith he has underweight.",
                systemUnderTest.getBmiMessage(LocalDate.of(1988, 12, 12),
                        55.0, 1.80));
    }
    @Test
    void testBerechneBMIWithOverweight() {
        Assertions.assertEquals("User has a BMI of 44.44 and therewith he has overweight.",
                systemUnderTest.getBmiMessage(LocalDate.of(2000, 12,12),
                        100.0, 1.50));
    }

    @Test
    void testBMIisUnexpectedValue() {
        RuntimeException ex = Assertions.assertThrows(IllegalStateException.class, () ->
                systemUnderTest.getBmiMessage(LocalDate.of(2000, 12, 12),
                        -100.0, 1.85));
        Assertions.assertEquals("BMI could not be calculated.", ex.getMessage());
    }

    @Test
    void testBerechneBMI() {
        Assertions.assertEquals(30.86,
                systemUnderTest.calculateBMI(100.0, 1.8));
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
        Assertions.assertEquals("User Peter not identified!", ex.getMessage());
    }

    private List<User> addTestUser() {
        List<User> users = new ArrayList<>();
        users.add(User.builder()
                .id(1)
                .name("Peter")
                .birthdate(LocalDate.of(2005, 12, 12))
                .weight(90.0)
                .height(1.85)
                .favouriteColor("yellow")
                .bmi(26.29)
                .build());

        users.add(User.builder()
                .id(2)
                .name("Florian")
                .birthdate(LocalDate.of(1988, 12, 12))
                .weight(70.0)
                .height(1.85)
                .favouriteColor("yellow")
                .bmi(20.45)
                .build());
        return users;
    }
}