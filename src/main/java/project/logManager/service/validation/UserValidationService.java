package project.logManager.service.validation;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import project.logManager.common.dto.UserRequestDto;
import project.logManager.common.enums.UserColorEnum;
import project.logManager.common.message.ErrorMessages;
import project.logManager.exception.FirstUserUnequalActorException;
import project.logManager.exception.UserNotFoundException;
import project.logManager.model.entity.User;
import project.logManager.model.repository.LogRepository;
import project.logManager.model.repository.UserRepository;
import project.logManager.service.model.LogService;
import project.logManager.service.model.UserService;

import java.util.List;
import java.util.Optional;

import static project.logManager.common.message.ErrorMessages.PARAMETER_IS_MISSING;

@Component
@RequiredArgsConstructor
public class UserValidationService {

  private final UserRepository userRepository;
  private final LogService logService;
  private final LogRepository logRepository;

  private static final Logger LOGGER = LogManager.getLogger(UserService.class);

  public void checkIfAnyEntriesAreNull(UserRequestDto allParameters) {
    if (allParameters.actor == null
        || allParameters.name == null
        || allParameters.birthdate == null
        || allParameters.favouriteColor == null
        || allParameters.weight == null
        || allParameters.height == null) {
      LOGGER.info(PARAMETER_IS_MISSING);
      throw new RuntimeException(PARAMETER_IS_MISSING);
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
      List<User> users = userRepository.findAll();
      if (users.isEmpty()) {
        if (!user.getName().equals(actor)) {
          if (onCreate) {
            LOGGER.warn(ErrorMessages.NO_USERS_YET + user.getName() + " ungleich " + actor);
            throw new FirstUserUnequalActorException(actor, user.getName());
          } else {
            LOGGER.error(String.format(ErrorMessages.USER_NOT_FOUND_ID, user.getId()));
            throw new RuntimeException(
                String.format(ErrorMessages.USER_NOT_FOUND_ID, user.getId()));
          }
        }
        return true;
      }
      return false;
    } catch (FirstUserUnequalActorException ex) {
      handleErsterUserUngleichActor(actor, ex);
      return false;
    }
  }

  private void handleErsterUserUngleichActor(String actor, FirstUserUnequalActorException er) {
    try {
      logService.addLog(ErrorMessages.USER_NOT_CREATED, "ERROR", actor);
    } catch (RuntimeException rex) {
      throw new RuntimeException(er.getMessage());
    }
  }

  public User checkIfNameExists(String name, boolean isCreate) {
    try {
      User activeUser = userRepository.findUserByName(name);
      if (activeUser == null) {
        LOGGER.info(String.format(ErrorMessages.USER_NOT_IDENTIFIED, name));
        throw new UserNotFoundException(name);
      }
      return activeUser;
    } catch (RuntimeException rex) {
      if (isCreate) {
        throw new RuntimeException(handleUserKonnteNichtAngelegtWerden(name, rex));
      } else {
        throw new RuntimeException(rex.getMessage());
      }
    }
  }

  private String handleUserKonnteNichtAngelegtWerden(String actor, RuntimeException ex) {
    LOGGER.error(ErrorMessages.USER_NOT_CREATED);
    logService.addLog(ErrorMessages.USER_NOT_CREATED, "ERROR", actor);
    return ex.getMessage();
  }

  public void checkIfUserToPostExists(String name) {
    try {
      if (userRepository.findUserByName(name) != null) {
        LOGGER.warn(String.format(ErrorMessages.USER_EXISTS, name));
        throw new RuntimeException(String.format(ErrorMessages.USER_EXISTS, name));
      }
    } catch (RuntimeException rex) {
      throw new RuntimeException(rex.getMessage());
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
