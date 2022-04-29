package project.userFeaturePortal.service.validation;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import project.userFeaturePortal.common.dto.user.UserRequestDto;
import project.userFeaturePortal.common.message.ErrorMessages;
import project.userFeaturePortal.common.message.InfoMessages;
import project.userFeaturePortal.exception.FirstUserUnequalActorException;
import project.userFeaturePortal.exception.ParameterNotPresentException;
import project.userFeaturePortal.exception.UserNotAllowedException;
import project.userFeaturePortal.exception.UserNotFoundException;
import project.userFeaturePortal.model.entity.User;
import project.userFeaturePortal.model.repository.BookRepository;
import project.userFeaturePortal.model.repository.LogRepository;
import project.userFeaturePortal.model.repository.UserRepository;
import project.userFeaturePortal.service.model.LogService;
import project.userFeaturePortal.service.model.UserService;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidationService {

  private static final Logger LOGGER = LogManager.getLogger(UserService.class);
  private final UserRepository userRepository;
  private final LogService logService;
  private final LogRepository logRepository;
  private final BookRepository bookRepository;

  public void checkIfAnyEntriesAreNull(UserRequestDto allParameters) {
    if (allParameters.actor == null
        || allParameters.actor.equals("")
        || allParameters.name == null
        || allParameters.name.equals("")
        || allParameters.birthdate == null
        || allParameters.birthdate.equals("")
        || allParameters.weight == null
        || allParameters.height == null) {
      LOGGER.info(ErrorMessages.PARAMETER_IS_MISSING);
      throw new ParameterNotPresentException();
    }
    LOGGER.debug(InfoMessages.PARAMETERS_ARE_VALID);
  }

  boolean checkIfUsersListIsEmpty() {
    List<User> usersList = userRepository.findAll();
    if (usersList.isEmpty()) {
      LOGGER.info(InfoMessages.LIST_IS_EMPTY);
      return true;
    }
    return false;
  }

  void checkIfActorEqualsUserToCreate(String actor, String user, boolean isActor) {
    if (!user.equals(actor)) {
      if (isActor) {
        LOGGER.warn(ErrorMessages.NO_USERS_YET + user + " unequal " + actor);
        throw new FirstUserUnequalActorException(actor, user);
      }
      LOGGER.error(String.format(ErrorMessages.USER_NOT_FOUND_NAME, user));
      throw new UserNotFoundException(user);
    }
    LOGGER.debug(InfoMessages.ACTOR_EQUALS_USER);
  }

  public User checkIfNameExists(String name, boolean isActor, String action) {
    User user = userRepository.findUserByName(name);
    if (user == null) {
      handleNameNotExist(isActor, action, name);
    }
    return user;
  }

  private void handleNameNotExist(boolean isActor, String action, String name) {
    if (isActor) {
      LOGGER.info(String.format(action, name));
      throw new UserNotAllowedException(String.format(action, name));
    }
    LOGGER.warn(String.format(ErrorMessages.USER_NOT_FOUND_NAME, name));
    throw new UserNotFoundException(name);
  }

  public void checkIfUserToPostExists(String name) {
    if (userRepository.findUserByName(name) != null) {
      LOGGER.warn(String.format(ErrorMessages.USER_EXISTS, name));
      throw new RuntimeException(String.format(ErrorMessages.USER_EXISTS, name));
    }
  }

  void checkIfUserToDeleteIdEqualsActorId(int userToDeleteId, int actorId) {
    if (userToDeleteId == actorId) {
      LOGGER.error(ErrorMessages.USER_DELETE_HIMSELF);
      throw new RuntimeException(ErrorMessages.USER_DELETE_HIMSELF);
    }
    LOGGER.info(InfoMessages.USER_CAN_BE_DELETED);
  }

  User checkIfIdExists(int id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isEmpty()) {
      LOGGER.info(String.format(ErrorMessages.USER_NOT_FOUND_ID, id));
      throw new RuntimeException(String.format(ErrorMessages.USER_NOT_FOUND_ID, id));
    }
    return user.get();
  }

  public void checkIfExistLogByUserToDelete(User userToDelete) {
    if (logService.existLogByUserToDelete(userToDelete)) {
      LOGGER.error(String.format(ErrorMessages.USER_REFERENCED, userToDelete.getName()));
      throw new RuntimeException(
          String.format(ErrorMessages.USER_REFERENCED, userToDelete.getName()));
    }
  }

  public void checkIfUserToDeleteEqualsActor(String name, String actorName) {
    User actor = userRepository.findUserByName(actorName);
    if (userRepository.findUserByName(name).equals(actor)) {
      LOGGER.error(ErrorMessages.USER_DELETE_HIMSELF);
      throw new RuntimeException(ErrorMessages.USER_DELETE_HIMSELF);
    }
  }

  public void checkIfUsersAreReferenced() {
    if (!logRepository.findAll().isEmpty()) {
      LOGGER.warn(ErrorMessages.USERS_REFERENCED);
      throw new RuntimeException(ErrorMessages.USERS_REFERENCED);
    }
  }

  public boolean validateUserToCreate(String name, String actor) {
    checkIfUserToPostExists(name);
    if (checkIfUsersListIsEmpty()) {
      checkIfActorEqualsUserToCreate(actor, name, true);
      return true;
    }
    return false;
  }

  public boolean validateActor(String actor, String action) {
    checkIfNameExists(actor, true, String.format(action, actor));
    return true;
  }

  public void validateDeletingById(int id, String actorName) {
    User userToDelete = checkIfIdExists(id);
    User actor = checkIfNameExists(actorName, true, ErrorMessages.USER_NOT_ALLOWED_DELETE_USER);
    checkIfUserToDeleteIdEqualsActorId(id, actor.getId());
    checkIfUsersListIsEmpty();
    checkIfExistLogByUserToDelete(userToDelete);
  }

  public User validateUserToDelete(String name, String actorName) {
    User user = checkIfNameExists(name, false, ErrorMessages.CANNOT_DELETE_USER);
    checkIfExistLogByUserToDelete(user);
    checkIfUserToDeleteEqualsActor(name, actorName);
    return user;
  }
}
