package project.userFeaturePortal.service.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.userFeaturePortal.common.dto.user.UserRequestDto;
import project.userFeaturePortal.common.message.ErrorMessages;
import project.userFeaturePortal.exception.FirstUserUnequalActorException;
import project.userFeaturePortal.exception.ParameterNotPresentException;
import project.userFeaturePortal.exception.UserNotAllowedException;
import project.userFeaturePortal.exception.UserNotFoundException;
import project.userFeaturePortal.model.entity.Log;
import project.userFeaturePortal.model.entity.User;
import project.userFeaturePortal.model.repository.LogRepository;
import project.userFeaturePortal.model.repository.UserRepository;
import project.userFeaturePortal.service.model.LogService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserValidationServiceTest {

  @InjectMocks UserValidationService systemUnderTest;

  @Mock UserRepository userRepository;

  @Mock LogService logService;

  @Mock LogRepository logRepository;

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
                        .name(null)
                        .birthdate("19.02.1995")
                        .weight(75.0)
                        .height(1.80)
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
            .build());
  }

  @Test
  void whenUsersListIsNotEmpty_ThenReturnFalse() {
    when(userRepository.findAll()).thenReturn(users);
    assertFalse(systemUnderTest.validateActor("Hans", "Hans"));
  }

  @Test
  void userToCreateDoesNotExist() {
    when(userRepository.findUserByName(anyString())).thenReturn(users.get(0));
    assertThrows(
        RuntimeException.class, () -> systemUnderTest.validateActor("Peter", "Hans"));
  }

  @Test
  void whenUserToCreateNotEqualActor_ThenThrowFirstUserUnequalActorException() {
    assertThrows(
        FirstUserUnequalActorException.class,
        () -> systemUnderTest.validateActor("Peter", "Hans"));
  }

  @Test
  void whenLogsExistByUserToDelete_ThenThrowException() {
    when(logService.existLogByUserToDelete(any())).thenReturn(true);
    when(userRepository.findUserByName("Peter")).thenReturn(users.get(0));
    assertThrows(RuntimeException.class, () -> systemUnderTest.validateUserToDelete("Peter", "Hans"));
  }

  @Test
  void userToDeleteIsValid() {
    when(userRepository.findUserByName("Peter")).thenReturn(users.get(0));
    when(userRepository.findUserByName("Florian")).thenReturn(users.get(1));
    systemUnderTest.validateUserToDelete("Peter", "Florian");
  }

  @Test
  void whenActorEqualsUserToDelete_ThenThrowException() {
    when(userRepository.findUserByName(anyString())).thenReturn(users.get(0));
    assertThrows(RuntimeException.class, () ->
            systemUnderTest.validateUserToDelete("Peter", "Peter"));
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

  @Test
  void testIdSuccessfullyFound() {
    Optional<User> user = Optional.ofNullable(User.builder()
            .id(1)
            .name("Peter")
            .birthdate(LocalDate.of(2005, 12, 12))
            .weight(90.0)
            .height(1.85)
            .bmi(26.29)
            .build());
    when(userRepository.findById(1)).thenReturn(user);
    systemUnderTest.checkIfIdExists(1);
  }

  @Test
  void whenIdNotFound_ThenThrowException() {
    assertThrows(RuntimeException.class, () ->
            systemUnderTest.checkIfIdExists(1));
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
            .bmi(26.29)
            .build());

    users.add(
        User.builder()
            .id(2)
            .name("Florian")
            .birthdate(LocalDate.of(1988, 12, 12))
            .weight(70.0)
            .height(1.85)
            .bmi(20.45)
            .build());
    return users;
  }
}
