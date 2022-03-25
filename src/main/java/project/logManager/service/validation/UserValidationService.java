package project.logManager.service.validation;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import project.logManager.common.dto.UserRequestDto;
import project.logManager.common.enums.UserColorEnum;
import project.logManager.common.message.ErrorMessages;
import project.logManager.exception.FirstUserUnequalActorException;
import project.logManager.exception.ParameterNotPresentException;
import project.logManager.exception.UserNotAllowedException;
import project.logManager.exception.UserNotFoundException;
import project.logManager.model.entity.User;
import project.logManager.model.repository.LogRepository;
import project.logManager.model.repository.UserRepository;
import project.logManager.service.model.LogService;
import project.logManager.service.model.UserService;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidationService {

  private final UserRepository userRepository;
  private final LogService logService;
  private final LogRepository logRepository;

  private static final Logger LOGGER = LogManager.getLogger(UserService.class);

  public void checkIfAnyEntriesAreNull(UserRequestDto allParameters) {
    if (allParameters.actor == null
        || allParameters.actor.equals("")
        || allParameters.name == null
        || allParameters.name.equals("")
        || allParameters.birthdate == null
        || allParameters.birthdate.equals("")
        || allParameters.favouriteColor == null
        || allParameters.favouriteColor.equals("")
        || allParameters.weight == null
        || allParameters.height == null) {
      LOGGER.info(ErrorMessages.PARAMETER_IS_MISSING);
      throw new ParameterNotPresentException(ErrorMessages.PARAMETER_IS_MISSING);
    }
  }

  public void validateFarbenEnum(String userFarben) {
    for (UserColorEnum farbenEnum : UserColorEnum.values()) {
      if (userFarben.equals(farbenEnum.getColor())) {
        return;
      }
    }
    LOGGER.error(ErrorMessages.COLOR_ILLEGAL, userFarben);
    throw new IllegalArgumentException(ErrorMessages.COLOR_ILLEGAL_PLUS_CHOICE);
  }

  public boolean checkIfUsersListIsEmpty(String actor, User user, boolean onCreate) {
    try {
      List<User> usersList = userRepository.findAll();
      handleUsersListIsEmpty(usersList, actor, user, onCreate);
      return false;
    } catch (RuntimeException ex) {
      LOGGER.warn(ErrorMessages.NO_USERS_YET + user.getName() + " ungleich " + actor);
      throw new FirstUserUnequalActorException(actor, user.getName());
    }
  }

  private void handleUsersListIsEmpty(
      List<User> usersList, String actor, User user, boolean onCreate) {
    if (usersList.isEmpty()) {
      if (!user.getName().equals(actor)) {
        if (onCreate) {
          LOGGER.warn(ErrorMessages.NO_USERS_YET + user.getName() + " ungleich " + actor);
          throw new RuntimeException();
        } else {
          LOGGER.error(String.format(ErrorMessages.USER_NOT_FOUND_ID, user.getId()));
          throw new RuntimeException(String.format(ErrorMessages.USER_NOT_FOUND_ID, user.getId()));
        }
      }
    }
  }

  public User checkIfNameExists(String name, boolean isCreate, String action) {
    try {
      User activeUser = userRepository.findUserByName(name);
      if (activeUser == null) {
        throw new RuntimeException();
      }
      return activeUser;
    } catch (RuntimeException ex) {
      return handleRuntimeExceptionIfNameNotExist(isCreate, action, name);
    }
  }

  private User handleRuntimeExceptionIfNameNotExist(boolean isCreate, String action, String name) {
    if (isCreate) {
      LOGGER.info(String.format(action, name));
      throw new UserNotAllowedException(String.format(action, name));
    } else {
      throw new UserNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND_NAME, name));
    }
  }

  public void checkIfUserToPostExists(String name) {
    userRepository.findUserByName(name);
    {
      LOGGER.warn(String.format(ErrorMessages.USER_EXISTS, name));
      throw new RuntimeException(String.format(ErrorMessages.USER_EXISTS, name));
    }
  }

  public void checkIfUserToDeleteIdEqualsActorId(int userToDeleteId, int actorId) {
    if (userToDeleteId == actorId) {
      LOGGER.error(ErrorMessages.USER_DELETE_HIMSELF);
      throw new RuntimeException(ErrorMessages.USER_DELETE_HIMSELF);
    }
  }

  public User checkIfIdExists(int id) {
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
    User userToDelete = userRepository.findUserByName(name);
    User actor = userRepository.findUserByName(actorName);
    if (userToDelete.equals(actor)) {
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
}
