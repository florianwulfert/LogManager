package project.logManager.service.model;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.logManager.common.message.ErrorMessages;
import project.logManager.common.message.InfoMessages;
import project.logManager.model.entity.User;
import project.logManager.model.repository.LogRepository;
import project.logManager.model.repository.UserRepository;
import project.logManager.service.validation.UserValidationService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {
    private final LogService logService;
    private final UserRepository userRepository;
    private final BmiService bmiService;
    private final LogRepository logRepository;
    private final UserValidationService userValidationService;

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    public String addUser(String actor, String name, LocalDate birthdate, double weight,
                          double height, String favouriteColor) {

        User user = User.builder()
                .name(name)
                .birthdate(birthdate)
                .weight(weight)
                .height(height)
                .favouriteColor(favouriteColor.toLowerCase())
                .bmi(bmiService.calculateBMI(weight, height))
                .build();

        userValidationService.validateFarbenEnum(favouriteColor.toLowerCase());
        userValidationService.checkIfUserToPostExists(name);
        if (userValidationService.checkIfUsersListIsEmpty(actor, user, true)) {
            saveUser(user, actor);
        } else {
            User activeUser = userValidationService.checkIfActorExists(actor, true);
            saveUser(user, activeUser.getName());
        }
        return bmiService.getBmiMessage(birthdate, weight, height);
    }

    public List<User> findUserList() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Integer id) {
        return userRepository.findById(id).isPresent() ? userRepository.findById(id) : Optional.empty();
    }

    public String deleteById(Integer id, String actorName) {
        User userToDelete = userValidationService.checkIfIdExists(id);
        User actor = userValidationService.checkIfActorExists(actorName, false);
        userValidationService.checkIfUserToDeleteIdEqualsActorId(id, actor.getId());
        userValidationService.checkIfUsersListIsEmpty(actor.getName(), userToDelete, false);
        userValidationService.checkIfExistLogByUserToDelete(userToDelete);

        userRepository.deleteById(id);

        logService.addLog(String.format(InfoMessages.USER_DELETED_ID, id), "WARNING", actorName);
        LOGGER.info(String.format(InfoMessages.USER_DELETED_ID, id));
        return String.format(InfoMessages.USER_DELETED_ID, id);
    }

    public String deleteByName(String name, String actorName) {
        if (name.equals(actorName)) {
            LOGGER.error(ErrorMessages.USER_DELETE_HIMSELF);
            throw new RuntimeException(ErrorMessages.USER_DELETE_HIMSELF);
        }
        User userToDelete = userRepository.findUserByName(name);
        User actor = userRepository.findUserByName(actorName);
        if (userToDelete == null) {
            LOGGER.error(String.format(ErrorMessages.USER_NOT_FOUND_NAME, name));
            throw new RuntimeException(String.format(ErrorMessages.USER_NOT_FOUND_NAME, name));
        }
        if (logService.existLogByUserToDelete(userToDelete)) {
            LOGGER.error(String.format(ErrorMessages.USER_REFERENCED, name));
            throw new RuntimeException(String.format(ErrorMessages.USER_REFERENCED, userToDelete.getName()));
        }
        if (actor == null) {
            LOGGER.error(String.format(ErrorMessages.USER_NOT_FOUND_NAME, actorName));
            throw new RuntimeException(String.format(ErrorMessages.USER_NOT_FOUND_NAME, actorName));
        }

        String deleteMessage = InfoMessages.USER_DELETED_NAME;
        userRepository.deleteById(userToDelete.getId());
        logService.addLog(String.format(deleteMessage, name), "WARNING", actorName);
        LOGGER.info(String.format(deleteMessage, name));
        return String.format(deleteMessage, name);
    }

    public String deleteAll() {
        if (!logRepository.findAll().isEmpty()) {
            LOGGER.warn(ErrorMessages.USERS_REFERENCED);
            throw new RuntimeException(ErrorMessages.USERS_REFERENCED);
        }
        userRepository.deleteAll();
        LOGGER.info(InfoMessages.ALL_USERS_DELETED);
        return InfoMessages.ALL_USERS_DELETED;
    }

    private void saveUser(User user, String actor) {
        userRepository.save(user);
        String bmi = bmiService.getBmiMessage(user.getBirthdate(), user.getWeight(), user.getHeight());
        logService.addLog(String.format(InfoMessages.USER_CREATED + "%s", user.getName(), bmi),
                "INFO", actor);
        LOGGER.info(String.format(InfoMessages.USER_CREATED + bmiService.getBmiMessage(
                user.getBirthdate(), user.getWeight(), user.getHeight()),
                user.getName()));
    }
}
