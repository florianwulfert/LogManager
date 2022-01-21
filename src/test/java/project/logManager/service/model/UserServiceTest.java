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
    void testAddUser() {
        Mockito.when(bmiService.getBmiMessage(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn("Test");
        Mockito.when(userValidationService.checkIfActorExists(Mockito.anyString())).thenReturn(users.get(1));
        systemUnderTest.addUser(users.get(1).getName(), "Peter", LocalDate.of
                        (1988, 12, 12), 90.0,
                1.85, "GELB");
        Mockito.verify(logService).addLog("User Peter was created. Test",
                "INFO", "Florian");
        Mockito.verify(userRepository).save(Mockito.any());
    }

    @Test
    void testUsersListIsEmpty() {
        Mockito.when(bmiService.getBmiMessage(Mockito.any(), Mockito.any(),
                Mockito.any())).thenReturn("Test");
        Mockito.when(userValidationService.checkIfUsersListIsEmpty(Mockito.anyString(), Mockito.any())).thenReturn(true);
        systemUnderTest.addUser(users.get(0).getName(), users.get(0).getName(),
                LocalDate.of(2000, 11, 18), 80,
                1.85, "blau");
        Mockito.verify(logService).addLog("User Peter was created. Test",
                "INFO", "Peter");
        Mockito.verify(userRepository).save(Mockito.any());
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
        Mockito.verify(logService).addLog("User with the ID 2 was deleted.",
                "WARNING", "Peter");
    }

    @Test
    void testIfActorIdIsNull() {
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(users.get(0)));
        UserNotFoundException ex = Assertions.assertThrows(UserNotFoundException.class, () ->
                systemUnderTest.deleteById(2, users.get(0).getName()));
        Assertions.assertEquals("User Peter not identified!", ex.getMessage());
    }

    @Test
    void testIfIdEqualsActorID() {
        Mockito.when(userRepository.findUserByName(users.get(0).getName()))
                .thenReturn(users.get(0));
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () ->
                systemUnderTest.deleteById(users.get(0).getId(), users.get(0).getName()));
        Assertions.assertEquals("User cannot delete himself!",
                ex.getMessage());
    }

    @Test
    void testIfUserToDeleteListIsEmpty() {
        Mockito.when(userRepository.findUserByName(users.get(0).getName())).
                thenReturn(users.get(0));
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () ->
                systemUnderTest.deleteById(users.get(1).getId(), users.get(0).getName()));
        Assertions.assertEquals("User with the ID 2 not found.",
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
        Assertions.assertEquals("User Florian cannot be deleted because he is referenced in another table!",
                ex.getMessage());
    }

    @Test
    void testIfNameToDeleteEqualsActorname() {
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () ->
                systemUnderTest.deleteByName(users.get(0).getName(), users.get(0).getName()));
        Assertions.assertEquals("User cannot delete himself!",
                ex.getMessage());
    }

    @Test
    void testIfUserToDeleteIsNull() {
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class,
                () -> systemUnderTest.deleteByName("Peter", "Florian"));
        Assertions.assertEquals("User named Peter not found!",
                ex.getMessage());
    }

    @Test
    void testIsUserToDeleteIsUsedSomewhereForName() {
        Mockito.when(userRepository.findUserByName(users.get(0).getName())).thenReturn(users.get(0));
        Mockito.when(logService.existLogByActorId(users.get(0))).thenReturn(true);
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class,
                () -> systemUnderTest.deleteByName("Peter", "Florian"));
        Assertions.assertEquals("User Peter cannot be deleted because he is referenced in another table!",
                ex.getMessage());
    }

    @Test
    void testIfActorNameIsNull() {
        Mockito.when(userRepository.findUserByName("Peter")).thenReturn(users.get(0));
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class,
                () -> systemUnderTest.deleteByName("Peter", "hallo"));
        Assertions.assertEquals("User named hallo not found!",
                ex.getMessage());
    }

    @Test
    void testDeleteByName() {
        Mockito.when(userRepository.findUserByName("Peter")).thenReturn(users.get(0));
        Mockito.when(userRepository.findUserByName("Florian")).thenReturn(users.get(1));
        systemUnderTest.deleteByName("Peter", "Florian");
        Mockito.verify(logService).addLog("User named Peter was deleted.",
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
        Assertions.assertEquals("User cannot be deleted because they are referenced in another table!",
                ex.getMessage());
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

