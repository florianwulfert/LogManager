package project.logManager.service.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.common.message.ErrorMessages;
import project.logManager.model.entity.Log;
import project.logManager.model.entity.User;
import project.logManager.model.repository.LogRepository;
import project.logManager.model.repository.UserRepository;
import project.logManager.service.model.LogService;
import project.logManager.service.model.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserValidationServiceTest {

  @InjectMocks UserValidationService systemUnderTest;

  @Mock UserService userService;

  @Mock UserRepository userRepository;

  @Mock LogService logService;

  @Mock LogRepository logRepository;

  List<User> users;

  @BeforeEach
  void init() {
    users = addTestUser();
  }

  @Test
  void testIfColorIsNotCorrect() {
    RuntimeException ex =
        Assertions.assertThrows(
            IllegalArgumentException.class, () -> systemUnderTest.validateFarbenEnum("gold"));
    Assertions.assertEquals(ErrorMessages.COLOR_ILLEGAL_PLUS_CHOICE, ex.getMessage());
  }

  @Test
  void testCheckIfUsersListIsEmpty() {
    Assertions.assertTrue(
        systemUnderTest.checkIfUsersListIsEmpty("Peter", users.get(0), true), "Test");
  }

  @Test
  void testCheckIfUsersListIsNotEmpty() {
    Mockito.when(userRepository.findAll()).thenReturn(users);
    Assertions.assertFalse(
        systemUnderTest.checkIfUsersListIsEmpty("Peter", users.get(0), false), "Test");
  }

  @Test
  void testIfUserNotEqualActorAndNoUsersYet() {
    Mockito.when(logService.addLog(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
        .thenThrow(RuntimeException.class);
    RuntimeException ex =
        Assertions.assertThrows(
            RuntimeException.class,
            () -> systemUnderTest.checkIfUsersListIsEmpty("Peter", users.get(1), true));
    Assertions.assertEquals(ErrorMessages.NO_USERS_YET + "Florian unequal Peter", ex.getMessage());
  }

  @Test
  void testIfUserNotEqualActorAndUserToDeleteIdNotFound() {
    RuntimeException ex =
        Assertions.assertThrows(
            RuntimeException.class,
            () -> systemUnderTest.checkIfUsersListIsEmpty("Peter", users.get(1), false));
    Assertions.assertEquals(String.format(ErrorMessages.USER_NOT_FOUND_ID, 2), ex.getMessage());
  }

  // Der Fall trifft aktuell nicht ein, wird aber aus TestCoverage-Gründen getestet
  @Test
  void testIfLogServiceDoesNotThrowException() {
    systemUnderTest.checkIfUsersListIsEmpty("Hänsel", users.get(0), true);
  }

  @Test
  void testIfActorNotExistsOnCreate() {
    Mockito.when(userRepository.findUserByName(Mockito.anyString())).thenReturn(null);
    RuntimeException ex =
        Assertions.assertThrows(
            RuntimeException.class,
            () -> systemUnderTest.checkIfNameExists(users.get(0).getName(), true));
    Assertions.assertEquals(
        String.format(ErrorMessages.USER_NOT_IDENTIFIED, "Peter"), ex.getMessage());
    Mockito.verify(logService)
        .addLog(ErrorMessages.USER_NOT_CREATED, "ERROR", users.get(0).getName());
  }

  @Test
  void testIfActorNotExistsOnDelete() {
    Mockito.when(userRepository.findUserByName(Mockito.anyString())).thenReturn(null);
    RuntimeException ex =
        Assertions.assertThrows(
            RuntimeException.class,
            () -> systemUnderTest.checkIfNameExists(users.get(0).getName(), false));
    Assertions.assertEquals(
        String.format(ErrorMessages.USER_NOT_IDENTIFIED, "Peter"), ex.getMessage());
  }

  @Test
  void testIfActorExists() {
    Mockito.when(userRepository.findUserByName(Mockito.anyString())).thenReturn(users.get(0));
    Assertions.assertEquals(
        users.get(0), systemUnderTest.checkIfNameExists(users.get(0).getName(), false));
  }

  @Test
  void testIfUserToPostExists() {
    Mockito.when(userRepository.findUserByName(Mockito.anyString())).thenReturn(users.get(0));
    RuntimeException ex =
        Assertions.assertThrows(
            RuntimeException.class, () -> systemUnderTest.checkIfUserToPostExists("Torsten"));
    Assertions.assertEquals(String.format(ErrorMessages.USER_EXISTS, "Torsten"), ex.getMessage());
  }

  @Test
  void testIfUserToPostIsNull() {
    systemUnderTest.checkIfUserToPostExists(users.get(0).getName());
  }

  @Test
  void testIfUserToDeleteIdEqualsActorId() {
    RuntimeException ex =
        Assertions.assertThrows(
            RuntimeException.class, () -> systemUnderTest.checkIfUserToDeleteIdEqualsActorId(1, 1));
    Assertions.assertEquals(ErrorMessages.USER_DELETE_HIMSELF, ex.getMessage());
  }

  @Test
  void testIfIdExists() {
    Mockito.when(userRepository.findById(Mockito.any()))
        .thenReturn(Optional.ofNullable(users.get(0)));
    systemUnderTest.checkIfIdExists(1);
  }

  @Test
  void testIfIdNotExists() {
    RuntimeException ex =
        Assertions.assertThrows(RuntimeException.class, () -> systemUnderTest.checkIfIdExists(1));
    Assertions.assertEquals(String.format(ErrorMessages.USER_NOT_FOUND_ID, 1), ex.getMessage());
  }

  @Test
  void testIfExistLogByUserToDelete() {
    Mockito.when(logService.existLogByUserToDelete(Mockito.any())).thenReturn(true);
    RuntimeException ex =
        Assertions.assertThrows(
            RuntimeException.class,
            () -> systemUnderTest.checkIfExistLogByUserToDelete(users.get(0)));
    Assertions.assertEquals(
        String.format(ErrorMessages.USER_REFERENCED, users.get(0).getName()), ex.getMessage());
  }

  @Test
  void testIfUserToDeleteEqualsActor() {
    Mockito.when(userRepository.findUserByName(Mockito.anyString())).thenReturn(users.get(0));
    Mockito.when(userRepository.findUserByName(Mockito.anyString())).thenReturn(users.get(0));
    RuntimeException ex =
        Assertions.assertThrows(
            RuntimeException.class,
            () ->
                systemUnderTest.checkIfUserToDeleteEqualsActor(
                    users.get(0).getName(), users.get(0).getName()));
    Assertions.assertEquals(ErrorMessages.USER_DELETE_HIMSELF, ex.getMessage());
  }

  @Test
  void testUsersAreReferenced() {
    List<Log> logs = new ArrayList<>();
    logs.add(
        Log.builder()
            .user(users.get(0))
            .timestamp(LocalDateTime.of(2000, 12, 12, 12, 12, 12))
            .message("Test")
            .severity("INFO")
            .build());
    Mockito.when(logRepository.findAll()).thenReturn(logs);
    RuntimeException ex =
        Assertions.assertThrows(
            RuntimeException.class, () -> systemUnderTest.checkIfUsersAreReferenced());
    Assertions.assertEquals(ErrorMessages.USERS_REFERENCED, ex.getMessage());
  }

  @Test
  void testUsersAreNotRefernced() {
    Mockito.when(logRepository.findAll()).thenReturn(new ArrayList<>());
    systemUnderTest.checkIfUsersAreReferenced();
  }

  private List<User> addTestUser() {
    List<User> users = new ArrayList<>();
    users.add(
        User.builder()
            .id(1)
            .name("Peter")
            .birthdate(LocalDate.of(2005, 12, 12))
            .weight(90.0)
            .height(1.85)
            .favouriteColor("yellow")
            .bmi(26.29)
            .build());

    users.add(
        User.builder()
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
