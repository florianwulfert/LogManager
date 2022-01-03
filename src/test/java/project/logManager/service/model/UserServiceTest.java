package project.logManager.service.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.model.entity.User;
import project.logManager.model.respository.UserRepository;
import project.logManager.service.validation.ValidationService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)


class UserServiceTest {

    @InjectMocks
    UserService systemUnderTest;

    @Mock
    UserRepository userRepository;

    @Mock
    ValidationService logValidationService;

    @Mock
    LogService logService;

    @Captor
    ArgumentCaptor<User> arg;

    @Test
    void testIfColorIsNotCorrect() {
        Mockito.when(logValidationService.validateFarbenEnum(Mockito.anyString())).thenReturn(false);
        Assertions.assertThrows(IllegalArgumentException.class, () -> systemUnderTest.addUser
                ("Florian", "Peter", LocalDate.of(1988, 12, 12), 90,
                1.85, "Lila"));
    }

    @Test
    void testFindByNameIsNotNull() {
        List<User> users = addTestUser();
        Mockito.when(logValidationService.validateFarbenEnum(Mockito.anyString())).thenReturn(true);
        Mockito.when(userRepository.findUserByName(Mockito.anyString())).thenReturn(users.get(0));
        Assertions.assertThrows(RuntimeException.class, () -> systemUnderTest.addUser
                ("Florian", "Peter", LocalDate.of(1988, 12, 12), 90,
                        1.85, "GELB"));
    }

    @Test
    void testIfActiveUserExists() {
        List<User> testUsers = addTestUser();
        Mockito.when(logValidationService.validateFarbenEnum(Mockito.anyString())).thenReturn(true);
        Mockito.when(userRepository.findUserByName(testUsers.get(0).getName())).thenReturn(null);
        Mockito.when(userRepository.findAll()).thenReturn(testUsers);
        Mockito.when(userRepository.findUserByName("Florian")).thenReturn(null);
        Assertions.assertThrows(RuntimeException.class, () -> systemUnderTest.addUser
                ("Florian", "Peter", LocalDate.of(1988, 12, 12), 90,
                1.85, "GELB"));
        Mockito.verify(logService).addLog("Der User konnte nicht angelegt werden",
                "ERROR", null);
    }

    @Test
    void testIfEverythingIsCorrectAtAddUser() {
        List<User> testUsers = addTestUser();
        Mockito.when(logValidationService.validateFarbenEnum(Mockito.anyString())).thenReturn(true);
        Mockito.when(userRepository.findUserByName(testUsers.get(0).getName())).thenReturn(null);
        Mockito.when(userRepository.findAll()).thenReturn(testUsers);
        Mockito.when(userRepository.findUserByName(testUsers.get(1).getName())).thenReturn(testUsers.get(1));
        systemUnderTest.addUser(testUsers.get(1).getName(), "Peter", LocalDate.of
                        (1988, 12, 12), 90,
                1.85, "GELB");
        Mockito.verify(userRepository).save(Mockito.any());
    }

    @Test
    void testUsersListIsEmpty() {
        List<User> users = addTestUser();
        Mockito.when((logValidationService.validateFarbenEnum(Mockito.anyString()))).thenReturn(true);
        systemUnderTest.addUser("Paul", users.get(0).getName(),
                LocalDate.of(2000, 11, 18), 80,
                1.85, "GRÜN");
        Mockito.verify(logService).addLog("Der User Peter wurde angelegt. Der User hat einen BMI " +
                "von 23.37 und ist somit normalgewichtig.", "INFO", null);
    }

    @Test
    void testFindUserList() {
        systemUnderTest.findUserList();
        Mockito.verify(userRepository).findAll();
    }

    @Test
    void testFindUserById() {
        systemUnderTest.findUserById(1);
        Mockito.verify(userRepository).findById(1);
    }

    @Test
    void testFindUserByName() {
        userRepository.findUserByName(Mockito.anyString());
        Mockito.verify(userRepository).findUserByName(Mockito.anyString());
    }

    @Test
    void testDeleteById() {
        List<User> testUsers = addTestUser();

        systemUnderTest.deleteById(1, testUsers.get(1).getName());
        Mockito.verify(logService).addLog("User mit der ID 1 wurde gelöscht.",
                "WARNING", "Hans");
    }

    @Test
    void testIfIdEqualsActorID() {
        List<User> testUsers = addTestUser();
        Assertions.assertThrows(RuntimeException.class, () ->
                systemUnderTest.deleteById(testUsers.get(0).getId(), testUsers.get(0).getName()));
    }

    @Test
    void testIfUserToDeleteListIsEmpty() {
        List<User> users = addTestUser();
        Assertions.assertThrows(RuntimeException.class, () ->
                systemUnderTest.deleteById(1, "Peter"));
    }

    @Test
    void testFindUserAndCalculateBMI() {
        List<User> testUser = addTestUser();
        systemUnderTest.findUserAndCalculateBMI(testUser.get(0).getName(), testUser.get(0).getGewicht(),
                testUser.get(0).getGroesse());
    }

    @Test
    void testBerechneBMI() {
        systemUnderTest.berechneBMI(100.0, 1.8);
    }

    @Test
    void testBerechneBMIWhenUserTooYoung() {
        List<User> testUser = addTestUser();
        Assertions.assertThrows(RuntimeException.class,
                () -> systemUnderTest.berechneBmiWithMessage(testUser.get(0).getGeburtsdatum(),
                        testUser.get(0).getGewicht(),
                        testUser.get(0).getGroesse()));
    }

    @Test
    void testBerechneBMIWithNormalWeight() {
        Assertions.assertEquals("Der User hat einen BMI von 23.14 und ist somit normalgewichtig.",
                systemUnderTest.berechneBmiWithMessage(LocalDate.of(1988, 12, 12),
                75.0, 1.80));
    }
    @Test
    void testBerechneBMIWithUnderweight() {
        Assertions.assertEquals("Der User hat einen BMI von 16.97 und ist somit untergewichtig.",
                systemUnderTest.berechneBmiWithMessage(LocalDate.of(1988, 12, 12),
                55.0, 1.80));
    }
    @Test
    void testBerechneBMIWithOverweight() {
        Assertions.assertEquals("Der User hat einen BMI von 44.44 und ist somit übergewichtig.",
        systemUnderTest.berechneBmiWithMessage(LocalDate.of(2000, 12,12),
                100.0, 1.50));
    }

    @Test
    void testBMIisUnexpectedValue() {

        Assertions.assertThrows(IllegalStateException.class, () ->
                systemUnderTest.berechneBmiWithMessage(LocalDate.of(2000, 12, 12),
                -100.0, 1.85));
    }

    private List<User> addTestUser() {
        List<User> users = new ArrayList<>();
        users.add(User.builder()
                .name("Peter")
                .geburtsdatum(LocalDate.of(2005, 12, 12))
                .gewicht(90)
                .groesse(1.85)
                .lieblingsfarbe("gelb")
                .build());

        users.add(User.builder()
                .name("Florian")
                .geburtsdatum(LocalDate.of(1988, 12, 12))
                .gewicht(90)
                .groesse(1.85)
                .lieblingsfarbe("gelb")
                .build());
        return users;
    }

}

