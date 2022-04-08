package project.userFeaturePortal.service.model;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.userFeaturePortal.common.dto.log.LogRequestDto;
import project.userFeaturePortal.common.dto.user.UserRequestDto;
import project.userFeaturePortal.common.message.ErrorMessages;
import project.userFeaturePortal.common.message.InfoMessages;
import project.userFeaturePortal.model.entity.User;
import project.userFeaturePortal.model.repository.UserRepository;
import project.userFeaturePortal.service.validation.UserValidationService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {

  private static final Logger LOGGER = LogManager.getLogger(UserService.class);
  private final LogService logService;
  private final UserRepository userRepository;
  private final BmiService bmiService;
  private final UserValidationService userValidationService;

  public String addUser(UserRequestDto userRequestDto) {
    userValidationService.checkIfAnyEntriesAreNull(userRequestDto);
    User user =
        User.builder()
            .name(userRequestDto.name)
            .birthdate(userRequestDto.getBirthdateAsLocalDate())
            .weight(userRequestDto.weight)
            .height(userRequestDto.height)
            .favouriteColor(userRequestDto.favouriteColor.toLowerCase())
            .bmi(bmiService.calculateBMI(userRequestDto.weight, userRequestDto.height))
            .build();

    userValidationService.validateFarbenEnum(user.getFavouriteColor().toLowerCase());
    userValidationService.checkIfUserToPostExists(user.getName());
    if (userValidationService.checkIfUsersListIsEmpty()) {
      userValidationService.checkIfActorEqualsUserToCreate(userRequestDto.actor, user, true);
      saveUser(user, userRequestDto.actor);
    } else {
      User activeUser =
          userValidationService.checkIfNameExists(
              userRequestDto.actor,
              true,
              String.format(ErrorMessages.USER_NOT_ALLOWED_CREATE_USER, userRequestDto.actor));
      saveUser(user, activeUser.getName());
    }
    return bmiService.calculateBmiAndGetBmiMessage(
        userRequestDto.getBirthdateAsLocalDate(), userRequestDto.weight, userRequestDto.height);
  }

  public List<User> findUserList() {
    List<User> users = userRepository.findAll();
    LOGGER.info(users);
    return users;
  }

  public Optional<User> findUserById(Integer id) {
    Optional<User> user = userRepository.findById(id).isPresent() ? userRepository.findById(id) : Optional.empty();
    LOGGER.info(user);
    return user;
  }

  public boolean findUserByName(String name) {
    User user = userRepository.findUserByName(name);
    if (user == null) {
     return false;
    }
    LOGGER.debug(String.format(InfoMessages.USER_FOUND, name));
    return true;
  }

  public void deleteById(Integer id, String actorName) {
    User userToDelete = userValidationService.checkIfIdExists(id);
    User actor =
        userValidationService.checkIfNameExists(
            actorName, true, ErrorMessages.USER_NOT_ALLOWED_DELETE_USER);
    userValidationService.checkIfUserToDeleteIdEqualsActorId(id, actor.getId());
    userValidationService.checkIfUsersListIsEmpty();
    userValidationService.checkIfExistLogByUserToDelete(userToDelete);

    userRepository.deleteById(id);
    saveLog(String.format(InfoMessages.USER_DELETED_ID, id), "WARNING", actorName);
    LOGGER.info(String.format(InfoMessages.USER_DELETED_ID, id));
  }

  public String deleteByName(String name, String actorName) {
    User user = userValidationService.checkIfNameExists(name, false, ErrorMessages.CANNOT_DELETE_USER);
    userValidationService.checkIfExistLogByUserToDelete(user);
    userValidationService.checkIfNameExists(actorName, true, ErrorMessages.USER_NOT_ALLOWED_DELETE_USER);
    userValidationService.checkIfUserToDeleteEqualsActor(name, actorName);

    userRepository.deleteById(user.getId());
    saveLog(String.format(InfoMessages.USER_DELETED_NAME, name), "WARNING", actorName);
    LOGGER.info(String.format(InfoMessages.USER_DELETED_NAME, name));
    return String.format(InfoMessages.USER_DELETED_NAME, name);
  }

  public String deleteAll() {
    userValidationService.checkIfUsersAreReferenced();
    userRepository.deleteAll();
    LOGGER.info(InfoMessages.ALL_USERS_DELETED);
    return InfoMessages.ALL_USERS_DELETED;
  }

  private void saveUser(User user, String actor) {
    userRepository.save(user);
    String bmi =
        bmiService.calculateBmiAndGetBmiMessage(
            user.getBirthdate(), user.getWeight(), user.getHeight());
    saveLog(String.format(InfoMessages.USER_CREATED + "%s", user.getName(), bmi), "INFO", actor);
    LOGGER.info(
        String.format(
            InfoMessages.USER_CREATED
                + bmiService.calculateBmiAndGetBmiMessage(
                user.getBirthdate(), user.getWeight(), user.getHeight()),
            user.getName()));
  }

  private void saveLog(String message, String severity, String actor) {
    LogRequestDto logRequestDto =
        LogRequestDto.builder()
            .message(message)
            .severity(severity)
            .user(actor)
            .build();
    LOGGER.info("Log " + logRequestDto + String.format(" was saved as %s", severity));
    logService.addLog(logRequestDto);
  }
}
