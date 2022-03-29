package project.logManager.service.validation;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import project.logManager.common.dto.UserRequestDto;
import project.logManager.common.enums.UserColorEnum;
import project.logManager.common.message.ErrorMessages;
import project.logManager.common.message.InfoMessages;
import project.logManager.exception.*;
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
    LOGGER.info(InfoMessages.PARAMETERS_ARE_VALID);
  }

  public void validateFarbenEnum(String userFarben) {
    for (UserColorEnum farbenEnum : UserColorEnum.values()) {
      if (userFarben.equals(farbenEnum.getColor())) {
        LOGGER.info("Color is valid!");
        return;
      }
    }
    LOGGER.error(ErrorMessages.COLOR_ILLEGAL, userFarben);
    throw new IllegalColorException(ErrorMessages.COLOR_ILLEGAL_PLUS_CHOICE);
  }

  public boolean checkIfUsersListIsEmpty() {
    List<User> usersList = userRepository.findAll();
    if (usersList.isEmpty()) {
      LOGGER.info(InfoMessages.LIST_IS_EMPTY);
      return true;
    }
    return false;
  }

  public void checkIfActorEqualsUserToCreate(String actor, User user, boolean isActor) {
    if (!user.getName().equals(actor)) {
      if (isActor) {
        LOGGER.warn(ErrorMessages.NO_USERS_YET + user.getName() + " unequal " + actor);
        throw new FirstUserUnequalActorException(actor, user.getName());
      }
      LOGGER.error(String.format(ErrorMessages.USER_NOT_FOUND_NAME, user.getName()));
      throw new UserNotFoundException(user.getName());
    }
    LOGGER.info(InfoMessages.ACTOR_EQUALS_USER);
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

  public void checkIfUserToDeleteIdEqualsActorId(int userToDeleteId, int actorId) {
    if (userToDeleteId == actorId) {
      LOGGER.error(ErrorMessages.USER_DELETE_HIMSELF);
      throw new RuntimeException(ErrorMessages.USER_DELETE_HIMSELF);
    }
    LOGGER.info(InfoMessages.USER_CAN_BE_DELETED);
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
}
