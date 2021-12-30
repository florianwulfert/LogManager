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
                1.85, "GELB"));
    }

    @Test
    void testFindByNameIsNotNull() {
        List<User> users = addTestUser();
        Mockito.when(logValidationService.validateFarbenEnum(Mockito.anyString())).thenReturn(true);
        Mockito.when(systemUnderTest.findUserByName(Mockito.anyString())).thenReturn(users);
        Assertions.assertThrows(RuntimeException.class, () -> systemUnderTest.addUser
                ("Florian", "Peter", LocalDate.of(1988, 12, 12), 90,
                        1.85, "GELB"));
    }

    @Test
    void testIfActiveUserExists() {
        Mockito.when(logValidationService.validateFarbenEnum(Mockito.anyString())).thenReturn(true);
        Assertions.assertThrows(RuntimeException.class, () -> systemUnderTest.addUser
                ("Florian", "Peter", LocalDate.of(1988, 12, 12), 90,
                1.85, "GELB"));
    }

    @Test
    void testIfEverythingIsCorrectAtAddUser() {
        List<User> testUsers = addTestUser();
        Mockito.when(logValidationService.validateFarbenEnum(Mockito.anyString())).thenReturn(true);
        Mockito.when(userRepository.findUserByName("Peter")).thenReturn(new ArrayList<>());
        Mockito.when(userRepository.findAll()).thenReturn(testUsers);
        Mockito.when(userRepository.findUserByName(testUsers.get(0).getName())).thenReturn(testUsers);
        systemUnderTest.addUser(testUsers.get(0).getName(), "Olaf", LocalDate.of
                        (1988, 12, 12), 90,
                1.85, "GELB");
        Mockito.verify(userRepository).save(Mockito.any());
    }

    @Test
    void testUserKonnteNichtAngelegtWerden() {
        List<User> users = addTestUser();
        Mockito.when((logValidationService.validateFarbenEnum(Mockito.anyString()))).thenReturn(true);
        Assertions.assertThrows(RuntimeException.class, () -> systemUnderTest.addUser("Paul", users.get(0).getName(),
                LocalDate.of(2000, 11, 18), 80,
                1.85, "GRÜN"));
        Mockito.verify(logService).addLog("Der User konnte nicht angelegt werden", "ERROR", null);
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
        systemUnderTest.findUserByName(Mockito.anyString());
        Mockito.verify(userRepository).findUserByName(Mockito.anyString());
    }

    @Test
    void testIfUserToDeleteIsEqualToActor() {
        List<User> testUser = addTestUser();
        systemUnderTest.findUserById(1);
        Assertions.assertThrows(RuntimeException.class, () ->
                systemUnderTest.deleteById(1, testUser.get(1)));
    }

    @Test
    void testIfUserIsEmpty() {
        List<User> testUser = addTestUser();
        Assertions.assertThrows(RuntimeException.class, () ->
                systemUnderTest.deleteById(1, testUser.get(1)));
    }

    /*@Test
    void testIfUserIsUsedSomewhere() {
        List<User> testUser = addTestUser();
        systemUnderTest.findUserById(1);
        logService.searchLogByActorId(testUser.get(0));
        Assertions.assertThrows(RuntimeException.class, () ->
                systemUnderTest.deleteById(1, testUser.get(1)));
    }

    @Test
    void testDeleteById() {
        List<User> testUser = addTestUser();
        systemUnderTest.findUserById(1);
        systemUnderTest.deleteById(1, testUser.get(0));
        Mockito.verify(userRepository).deleteById(1);

    }*/

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
        systemUnderTest.berechneBmiWithMessage(LocalDate.of(1988, 12, 12),
                75.0, 1.80);
    }
    @Test
    void testBerechneBMIWithUnderweight() {
        systemUnderTest.berechneBmiWithMessage(LocalDate.of(1988, 12, 12),
                55.0, 1.80);
    }
    @Test
    void testBerechneBMIWithOverweight() {
        systemUnderTest.berechneBmiWithMessage(LocalDate.of(2000, 12,12),
                100.0, 1.50);
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

