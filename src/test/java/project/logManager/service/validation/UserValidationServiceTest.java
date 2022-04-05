package project.logManager.service.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.common.dto.user.UserRequestDto;
import project.logManager.common.message.ErrorMessages;
import project.logManager.exception.FirstUserUnequalActorException;
import project.logManager.exception.IllegalColorException;
import project.logManager.exception.ParameterNotPresentException;
import project.logManager.exception.UserNotAllowedException;
import project.logManager.exception.UserNotFoundException;
import project.logManager.model.entity.Log;
import project.logManager.model.entity.User;
import project.logManager.model.repository.LogRepository;
import project.logManager.model.repository.UserRepository;
import project.logManager.service.model.LogService;
import project.logManager.service.model.UserService;

@ExtendWith(MockitoExtension.class)
class UserValidationServiceTest {

  @InjectMocks
  UserValidationService systemUnderTest;

  @Mock
  UserService userService;

  @Mock
  UserRepository userRepository;

  @Mock
  LogService logService;

  @Mock
  LogRepository logRepository;

  List<User> users;

  @BeforeEach
  void init() {
    users = addTestUser();
  }

  @Test
  void testIfAnyEntriesAreNull() {
    ParameterNotPresentException ex =
        assertThrows(
            ParameterNotPresentException.class,
            () ->
                systemUnderTest.checkIfAnyEntriesAreNull(
                    UserRequestDto.builder()
                        .actor("Peter")
                        .name("Hans")
                        .birthdate("19.02.1995")
                        .weight(75.0)
                        .height(1.80)
                        .favouriteColor("")
                        .build()));
    Assertions.assertEquals(ErrorMessages.PARAMETER_IS_MISSING, ex.getMessage());
  }

  @Test
  void AllParametersAreCorrect() {
    systemUnderTest.checkIfAnyEntriesAreNull(
        UserRequestDto.builder()
            .actor("Peter")
            .name("Hans")
            .birthdate("19.02.1995")
            .weight(75.0)
            .height(1.80)
            .favouriteColor("blue")
            .build());
  }

  @Test
  void testIfColorIsNotCorrect() {
    IllegalColorException ex =
        Assertions.assertThrows(
            IllegalColorException.class, () -> systemUnderTest.validateFarbenEnum("gold"));
    Assertions.assertEquals(ErrorMessages.COLOR_ILLEGAL_PLUS_CHOICE, ex.getMessage());
  }

  @Test
  void ColorIsCorrect() {
    systemUnderTest.validateFarbenEnum("blue");
  }

  @Test
  void whenUsersListIsNotEmpty_ThenReturnFalse() {
    when(userRepository.findAll()).thenReturn(users);
    assertFalse(systemUnderTest.checkIfUsersListIsEmpty());
  }

  @Test
  void whenUsersListIsEmpty_ThenReturnTrue() {
    assertTrue(systemUnderTest.checkIfUsersListIsEmpty());
  }

  @Test
  void userIsEqualToActor() {
    systemUnderTest.checkIfActorEqualsUserToCreate(users.get(0).getName(), users.get(0), true);
  }

  @Test
  void ActorIsNotEqualUserButWantsToAct() {
    FirstUserUnequalActorException ex =
        assertThrows(
            FirstUserUnequalActorException.class,
            () ->
                systemUnderTest.checkIfActorEqualsUserToCreate(
                    users.get(0).getName(), users.get(1), true));

    assertEquals(
        ErrorMessages.NO_USERS_YET + users.get(1).getName() + " unequal " + users.get(0).getName(),
        ex.getMessage());
  }

  @Test
  void ActorIsNotEqualUserAndOneWasNotFound() {
    UserNotFoundException ex =
        assertThrows(
            UserNotFoundException.class,
            () -> systemUnderTest.checkIfActorEqualsUserToCreate("Heinrich", users.get(0), false));

    assertEquals(String.format(ErrorMessages.USER_NOT_FOUND_NAME, users.get(0).getName()), ex.getMessage());
  }

  @Test
  void testNameExists() {
    when(userRepository.findUserByName(anyString())).thenReturn(users.get(0));
    assertEquals(
        users.get(0),
        systemUnderTest.checkIfNameExists(
            users.get(0).getName(), false, ErrorMessages.USER_NOT_ALLOWED_DELETE_USER));
  }

  @Test
  void testUserNotFound() {
    UserNotFoundException ex =
        assertThrows(
            UserNotFoundException.class,
            () ->
                systemUnderTest.checkIfNameExists(
                    "Heinrich",
                    false,
                    String.format(ErrorMessages.USER_NOT_FOUND_NAME, "Heinrich")));
    assertEquals(String.format(ErrorMessages.USER_NOT_FOUND_NAME, "Heinrich"), ex.getMessage());
  }

  @Test
  void testUserNotAllowed() {
    UserNotAllowedException ex =
        assertThrows(
            UserNotAllowedException.class,
            () ->
                systemUnderTest.checkIfNameExists(
                    "Heinrich",
                    true,
                    String.format(ErrorMessages.USER_NOT_ALLOWED_CREATE_USER, "Heinrich")));
    assertEquals(
        String.format(ErrorMessages.USER_NOT_ALLOWED_CREATE_USER, "Heinrich"), ex.getMessage());
  }

  @Test
  void testWhenUserToPostAlreadyExists() {
    when(userRepository.findUserByName(anyString())).thenReturn(users.get(0));
    RuntimeException ex =
        assertThrows(
            RuntimeException.class, () -> systemUnderTest.checkIfUserToPostExists("Torsten"));
    assertEquals(String.format(ErrorMessages.USER_EXISTS, "Torsten"), ex.getMessage());
  }

  @Test
  void testWhenUserToPostIsNull() {
    when(userRepository.findUserByName(anyString())).thenReturn(users.get(0));
    RuntimeException ex =
        assertThrows(
            RuntimeException.class, () -> systemUnderTest.checkIfUserToPostExists("Peter"));
    assertEquals(String.format(ErrorMessages.USER_EXISTS, "Peter"), ex.getMessage());
  }

  @Test
  void testIfUserToDeleteIdEqualsActorId() {
    RuntimeException ex =
        assertThrows(
            RuntimeException.class, () -> systemUnderTest.checkIfUserToDeleteIdEqualsActorId(1, 1));
    assertEquals(ErrorMessages.USER_DELETE_HIMSELF, ex.getMessage());
  }

  @Test
  void UserToDeleteIdNotEqualActorId() {
    systemUnderTest.checkIfUserToDeleteIdEqualsActorId(5, 1);
  }

  @Test
  void testIfIdExists() {
    when(userRepository.findById(any())).thenReturn(Optional.ofNullable(users.get(0)));
    systemUnderTest.checkIfIdExists(1);
  }

  @Test
  void testIfIdNotExists() {
    RuntimeException ex =
        assertThrows(RuntimeException.class, () -> systemUnderTest.checkIfIdExists(1));
    assertEquals(String.format(ErrorMessages.USER_NOT_FOUND_ID, 1), ex.getMessage());
  }

  @Test
  void testIfExistLogByUserToDelete() {
    when(logService.existLogByUserToDelete(any())).thenReturn(true);
    RuntimeException ex =
        assertThrows(
            RuntimeException.class,
            () -> systemUnderTest.checkIfExistLogByUserToDelete(users.get(0)));
    assertEquals(
        String.format(ErrorMessages.USER_REFERENCED, users.get(0).getName()), ex.getMessage());
  }

  @Test
  void testIfUserToDeleteEqualsActor() {
    when(userRepository.findUserByName(anyString())).thenReturn(users.get(0));
    when(userRepository.findUserByName(anyString())).thenReturn(users.get(0));
    RuntimeException ex =
        assertThrows(
            RuntimeException.class,
            () ->
                systemUnderTest.checkIfUserToDeleteEqualsActor(
                    users.get(0).getName(), users.get(0).getName()));
    assertEquals(ErrorMessages.USER_DELETE_HIMSELF, ex.getMessage());
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
    when(logRepository.findAll()).thenReturn(logs);
    RuntimeException ex =
        assertThrows(RuntimeException.class, () -> systemUnderTest.checkIfUsersAreReferenced());
    assertEquals(ErrorMessages.USERS_REFERENCED, ex.getMessage());
  }

  @Test
  void testUsersAreNotReferenced() {
    when(logRepository.findAll()).thenReturn(new ArrayList<>());
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
