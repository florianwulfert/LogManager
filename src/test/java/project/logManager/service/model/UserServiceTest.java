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
import project.logManager.model.entity.Log;
import project.logManager.model.entity.User;
import project.logManager.model.repository.LogRepository;
import project.logManager.model.repository.UserRepository;
import project.logManager.service.validation.UserValidationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    BmiService bmiService;

    @Mock
    LogRepository logRepository;

    @Mock
    UserValidationService userValidationService;

    @Mock
    LogService logService;

    List<User> users;

    @BeforeEach
    void init() {
        users = addTestUser();
    }

    @Test
    void testColorIsIncorrect() {
        Mockito.when(userValidationService.validateFarbenEnum(Mockito.anyString())).thenReturn(false);
        systemUnderTest.addUser(users.get(1).getName(), "Peter", LocalDate.of
                (1988, 12, 12), 90.0,
                1.85, "GELB");
        Mockito.verify(userValidationService).handleFarbeNichtZugelassen("GELB");
    }

    @Test
    void testAddUser() {
        Mockito.when(userValidationService.validateFarbenEnum(Mockito.anyString())).thenReturn(true);
        Mockito.when(bmiService.getBmiMessage(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn("Test");
        Mockito.when(userValidationService.checkIfActorExists(Mockito.anyString())).thenReturn(users.get(1));
        systemUnderTest.addUser(users.get(1).getName(), "Peter", LocalDate.of
                        (1988, 12, 12), 90.0,
                1.85, "GELB");
        Mockito.verify(logService).addLog("Der User Peter wurde angelegt. Test",
                "INFO", "Florian");
        Mockito.verify(userRepository).save(Mockito.any());
    }

    @Test
    void testUsersListIsEmpty() {
        Mockito.when((userValidationService.validateFarbenEnum(Mockito.anyString()))).thenReturn(true);
        Mockito.when(bmiService.getBmiMessage(Mockito.any(), Mockito.any(),
                Mockito.any())).thenReturn("Test");
        Mockito.when(userValidationService.checkIfActorExists(Mockito.anyString())).thenReturn(users.get(0));
        systemUnderTest.addUser(users.get(0).getName(), users.get(0).getName(),
                LocalDate.of(2000, 11, 18), 80,
                1.85, "blau");
        Mockito.verify(logService).addLog("Der User Peter wurde angelegt. Test", "INFO", "Peter");
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
    void testIfActorIdIsNull() {
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(users.get(0)));
        UserNotFoundException ex = Assertions.assertThrows(UserNotFoundException.class, () ->
                systemUnderTest.deleteById(2, users.get(0).getName()));
        Assertions.assertEquals("User Peter konnte nicht identifiziert werden!", ex.getMessage());
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
    void testIfActorNameIsNull() {
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
    void testDeleteAll() {
        systemUnderTest.deleteAll();
        Mockito.verify(userRepository).deleteAll();
    }

    @Test
    void testIfLogsExistForDeleteAll() {
        List<Log> testLogs = new ArrayList<>();
        testLogs.add(Log
                .builder()
                .message("Test")
                .severity("INFO")
                .id(1)
                .timestamp(LocalDateTime.of(2000, 12, 12, 12, 12, 12))
                .build());
        Mockito.when(logRepository.findAll()).thenReturn(testLogs);
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () ->
                systemUnderTest.deleteAll());
        Assertions.assertEquals("User können nicht gelöscht werden, da sie in einer anderen Tabelle " +
                "referenziert werden", ex.getMessage());
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

