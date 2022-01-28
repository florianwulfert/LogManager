package project.logManager.service.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.common.message.ErrorMessages;
import project.logManager.common.message.InfoMessages;
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
        Mockito.when(bmiService.getBmiMessage(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn("User has a BMI of 24.07 and therewith he has normal weight.");
        Mockito.when(userValidationService.checkIfNameExists(Mockito.anyString(), true)).thenReturn(users.get(1));
        systemUnderTest.addUser(users.get(0).getName(), "Florian", LocalDate.of
                        (1988, 12, 12), 90.0,
                1.85, "YELLOW");
        Mockito.verify(logService).addLog(String.format(InfoMessages.USER_CREATED + InfoMessages.BMI_MESSAGE,
                "Florian", 24.07) + InfoMessages.NORMAL_WEIGHT, "INFO", "Florian");
        Mockito.verify(userRepository).save(Mockito.any());
    }

    @Test
    void testUsersListIsEmpty() {
        Mockito.when(bmiService.getBmiMessage(Mockito.any(), Mockito.any(),
                Mockito.any())).thenReturn("User has a BMI of 24.07 and therewith he has normal weight.");
        Mockito.when(userValidationService.checkIfUsersListIsEmpty(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(true);
        systemUnderTest.addUser("Florian", "Florian", LocalDate.of(2000, 11, 18), 80,
                1.85, "blau");
        Mockito.verify(logService).addLog(String.format(InfoMessages.USER_CREATED + InfoMessages.BMI_MESSAGE,
                "Florian", 24.07) + InfoMessages.NORMAL_WEIGHT, "INFO", "Florian");
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
        Mockito.when(userRepository.findUserByName(Mockito.anyString())).thenReturn(users.get(0));
        systemUnderTest.deleteById(1, users.get(1).getName());
        Mockito.verify(logService).addLog(String.format(InfoMessages.USER_DELETED_ID, 1), "WARNING", "Florian");
    }

    @Test
    void testIfIdEqualsActorID() {
        Mockito.when(userRepository.findUserByName(users.get(0).getName()))
                .thenReturn(users.get(0));
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () ->
                systemUnderTest.deleteById(users.get(0).getId(), users.get(0).getName()));
        Assertions.assertEquals(ErrorMessages.USER_DELETE_HIMSELF, ex.getMessage());
    }

    @Test
    void testIfUserToDeleteListIsEmpty() {
        Mockito.when(userRepository.findUserByName(users.get(0).getName())).
                thenReturn(users.get(0));
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () ->
                systemUnderTest.deleteById(8, users.get(0).getName()));
        Assertions.assertEquals(String.format(ErrorMessages.USER_NOT_FOUND_ID, 8), ex.getMessage());
    }

    @Test
    void testIfUserToDeleteIsUsedSomewhereForId() {
        Mockito.when(userRepository.findUserByName(users.get(0).getName()))
                .thenReturn(users.get(0));
        Mockito.when(userRepository.findById(users.get(1).getId()))
                .thenReturn(Optional.ofNullable(users.get(1)));
        Mockito.when(logService.existLogByUserToDelete(users.get(1))).thenReturn(true);
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () ->
                systemUnderTest.deleteById(2, "Peter"));
        Assertions.assertEquals(String.format(ErrorMessages.USER_REFERENCED, "Florian"), ex.getMessage());
    }

    @Test
    void testIfNameToDeleteEqualsActorname() {
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () ->
                systemUnderTest.deleteByName(users.get(0).getName(), users.get(0).getName()));
        Assertions.assertEquals(ErrorMessages.USER_DELETE_HIMSELF, ex.getMessage());
    }

    @Test
    void testIfUserToDeleteIsNull() {
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class,
                () -> systemUnderTest.deleteByName("Hans", "Florian"));
        Assertions.assertEquals(String.format(ErrorMessages.USER_NOT_FOUND_NAME, "Hans"), ex.getMessage());
    }

    @Test
    void testIsUserToDeleteIsUsedSomewhereForName() {
        Mockito.when(userRepository.findUserByName(users.get(1).getName())).thenReturn(users.get(1));
        Mockito.when(logService.existLogByUserToDelete(users.get(1))).thenReturn(true);
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class,
                () -> systemUnderTest.deleteByName("Florian", "Peter"));
        Assertions.assertEquals(String.format(ErrorMessages.USER_REFERENCED, "Florian"), ex.getMessage());
    }

    @Test
    void testIfActorNameIsNull() {
        Mockito.when(userRepository.findUserByName("Peter")).thenReturn(users.get(0));
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class,
                () -> systemUnderTest.deleteByName("Peter", "Hans"));
        Assertions.assertEquals(String.format(ErrorMessages.USER_NOT_FOUND_NAME, "Hans"), ex.getMessage());
    }

    @Test
    void testDeleteByName() {
        Mockito.when(userRepository.findUserByName("Petra")).thenReturn(users.get(1));
        Mockito.when(userRepository.findUserByName("Peter")).thenReturn(users.get(0));
        systemUnderTest.deleteByName("Petra", "Peter");
        Mockito.verify(logService).addLog(String.format(InfoMessages.USER_DELETED_NAME, "Petra"), "WARNING", "Peter");
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
        Assertions.assertEquals(ErrorMessages.USERS_REFERENCED, ex.getMessage());
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

