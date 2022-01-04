package project.logManager.service.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.model.entity.User;
import project.logManager.model.respository.UserRepository;
import project.logManager.service.validation.ValidationService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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

    List<User> users;

    @BeforeEach
    void init() {
        users = addTestUser();
    }

    @Test
    void testIfColorIsNotCorrect() {
        Mockito.when(logValidationService.validateFarbenEnum(Mockito.anyString())).thenReturn(false);
        RuntimeException ex = Assertions.assertThrows(IllegalArgumentException.class, () -> systemUnderTest.addUser
                ("Florian", "Peter", LocalDate.of(1988, 12, 12), 90,
                1.85, "Lila"));
        Assertions.assertEquals("Illegal color!", ex.getMessage());
    }

    @Test
    void testFindByNameIsNotNull() {
        Mockito.when(logValidationService.validateFarbenEnum(Mockito.anyString())).thenReturn(true);
        Mockito.when(userRepository.findUserByName(Mockito.anyString())).thenReturn(users.get(0));
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () -> systemUnderTest.addUser
                ("Florian", "Peter", LocalDate.of(1988, 12, 12), 90,
                        1.85, "GELB"));
        Assertions.assertEquals("User Peter bereits vorhanden", ex.getMessage());
    }

    @Test
    void testIfActiveUserExists() {
        Mockito.when(logValidationService.validateFarbenEnum(Mockito.anyString())).thenReturn(true);
        Mockito.when(userRepository.findUserByName(users.get(0).getName())).thenReturn(null);
        Mockito.when(userRepository.findAll()).thenReturn(users);
        Mockito.when(userRepository.findUserByName("Florian")).thenReturn(null);
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () -> systemUnderTest.addUser
                ("Florian", "Peter", LocalDate.of(1988, 12, 12), 90,
                1.85, "GELB"));
        Assertions.assertEquals("User Florian nicht gefunden", ex.getMessage());
        Mockito.verify(logService).addLog("Der User konnte nicht angelegt werden",
                "ERROR", null);
    }

    @Test
    void testIfEverythingIsCorrectAtAddUser() {
        Mockito.when(logValidationService.validateFarbenEnum(Mockito.anyString())).thenReturn(true);
        Mockito.when(userRepository.findUserByName(users.get(0).getName())).thenReturn(null);
        Mockito.when(userRepository.findAll()).thenReturn(users);
        Mockito.when(userRepository.findUserByName(users.get(1).getName())).thenReturn(users.get(1));
        systemUnderTest.addUser(users.get(1).getName(), "Peter", LocalDate.of
                        (1988, 12, 12), 90,
                1.85, "GELB");
        Mockito.verify(userRepository).save(Mockito.any());
    }

    @Test
    void testUsersListIsEmpty() {
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
        Mockito.when(userRepository.findUserByName("Peter")).thenReturn(users.get(0));
        Mockito.when(userRepository.findById(2)).thenReturn(Optional.ofNullable(users.get(1)));
        Mockito.when(logService.existLogByActorId(users.get(1))).thenReturn(false);
        systemUnderTest.deleteById(2, users.get(0).getName());
        Mockito.verify(logService).addLog("User mit der ID 2 wurde gelöscht.",
                "WARNING", "Peter");
    }

    @Test
    void testIfIdEqualsActorID() {
        Mockito.when(userRepository.findUserByName(users.get(0).getName()))
                .thenReturn(users.get(0));
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () ->
                systemUnderTest.deleteById(users.get(0).getId(), users.get(0).getName()));
        Assertions.assertEquals("Ein User kann sich nicht selbst löschen!",
                ex.getMessage());
    }

    @Test
    void testIfUserToDeleteListIsEmpty() {
        Mockito.when(userRepository.findUserByName(users.get(0).getName())).
                thenReturn(users.get(0));
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () ->
                systemUnderTest.deleteById(users.get(1).getId(), users.get(0).getName()));
        Assertions.assertEquals("User mit der ID 2 konnte nicht gefunden werden",
                ex.getMessage());
    }

    @Test
    void testIfUserToDeleteIsUsedSomewhereForId() {
        Mockito.when(userRepository.findUserByName(users.get(0).getName()))
                .thenReturn(users.get(0));
        Mockito.when(userRepository.findById(users.get(1).getId()))
                .thenReturn(Optional.ofNullable(users.get(1)));
        Mockito.when(logService.existLogByActorId(users.get(1))).thenReturn(true);
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () ->
                systemUnderTest.deleteById(2, "Peter"));
        Assertions.assertEquals("User Florian kann nicht gelöscht werden, " +
                "da er in einer anderen Tabelle referenziert wird!", ex.getMessage());
    }

    @Test
    void testIfNameToDeleteEqualsActorname() {
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () ->
                systemUnderTest.deleteByName(users.get(0).getName(), users.get(0).getName()));
        Assertions.assertEquals("Ein User kann sich nicht selbst löschen!",
                ex.getMessage());
    }

    @Test
    void testIfUserToDeleteIsNull() {
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class,
                () -> systemUnderTest.deleteByName("Peter", "Florian"));
        Assertions.assertEquals("User mit dem Namen Peter konnte nicht gefunden werden",
                ex.getMessage());
    }

    @Test
    void testIsUserToDeleteIsUsedSomewhereForName() {
        Mockito.when(userRepository.findUserByName(users.get(0).getName())).thenReturn(users.get(0));
        Mockito.when(logService.existLogByActorId(users.get(0))).thenReturn(true);
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class,
                () -> systemUnderTest.deleteByName("Peter", "Florian"));
        Assertions.assertEquals("User Peter kann nicht gelöscht werden, " +
                "da er in einer anderen Tabelle referenziert wird!", ex.getMessage());
    }

    @Test
    void testIfActorIsNull() {
        Mockito.when(userRepository.findUserByName("Peter")).thenReturn(users.get(0));
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class,
                () -> systemUnderTest.deleteByName("Peter", "hallo"));
        Assertions.assertEquals("User mit dem Namen hallo konnte nicht gefunden werden",
                ex.getMessage());
    }

    @Test
    void testDeleteByName() {
        Mockito.when(userRepository.findUserByName("Peter")).thenReturn(users.get(0));
        Mockito.when(userRepository.findUserByName("Florian")).thenReturn(users.get(1));
        systemUnderTest.deleteByName("Peter", "Florian");
        Mockito.verify(logService).addLog("User mit dem Namen Peter wurde gelöscht",
                "WARNING", "Florian");
    }

    @Test
    void testFindUserAndCalculateBMI() {
        systemUnderTest.findUserAndCalculateBMI(users.get(0).getName(), users.get(0).getGewicht(),
                users.get(0).getGroesse());
    }

    @Test
    void testBerechneBMI() {
        systemUnderTest.berechneBMI(100.0, 1.8);
    }

    @Test
    void testBerechneBMIWhenUserTooYoung() {
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class,
                () -> systemUnderTest.berechneBmiWithMessage(users.get(0).getGeburtsdatum(),
                        users.get(0).getGewicht(),
                        users.get(0).getGroesse()));
        Assertions.assertEquals("Der User ist zu jung für eine genaue Bestimmung des BMI.",
                ex.getMessage());
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
        RuntimeException ex = Assertions.assertThrows(IllegalStateException.class, () ->
                systemUnderTest.berechneBmiWithMessage(LocalDate.of(2000, 12, 12),
                -100.0, 1.85));
        Assertions.assertEquals("Unexpected value", ex.getMessage());
    }

    private List<User> addTestUser() {
        List<User> users = new ArrayList<>();
        users.add(User.builder()
                .id(1)
                .name("Peter")
                .geburtsdatum(LocalDate.of(2005, 12, 12))
                .gewicht(90)
                .groesse(1.85)
                .lieblingsfarbe("gelb")
                .build());

        users.add(User.builder()
                .id(2)
                .name("Florian")
                .geburtsdatum(LocalDate.of(1988, 12, 12))
                .gewicht(90)
                .groesse(1.85)
                .lieblingsfarbe("gelb")
                .build());
        return users;
    }

}

