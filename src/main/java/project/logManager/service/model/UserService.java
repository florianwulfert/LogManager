package project.logManager.service.model;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.logManager.exception.UserNotFoundException;
import project.logManager.model.entity.User;
import project.logManager.model.repository.LogRepository;
import project.logManager.model.repository.UserRepository;
import project.logManager.service.validation.UserValidationService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static project.logManager.common.message.Messages.*;

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
        if (userValidationService.checkIfUsersListIsEmpty(actor, user)) {
            saveUser(user, actor);
        } else {
            User activeUser = userValidationService.checkIfActorExists(actor);
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
        Optional<User> user = findUserById(id);
        User actor = userRepository.findUserByName(actorName);
        if (actor == null) {
            LOGGER.warn(String.format(USER_NOT_IDENTIFIED, actorName));
            throw new UserNotFoundException(actorName);
        }
        if (id.equals(actor.getId())) {
            LOGGER.error(USER_DELETE_HIMSELF);
            throw new RuntimeException(USER_DELETE_HIMSELF);
        }
        if (user.isEmpty()) {
            LOGGER.error(String.format(USER_NOT_FOUND_ID, id));
            throw new RuntimeException(String.format(USER_NOT_FOUND_ID, id));
        } else {
            if (logService.existLogByActorId(user.get())) {
                LOGGER.error(String.format(USER_REFERENCED, user.get().getName()));
                throw new RuntimeException(String.format(USER_REFERENCED, user.get().getName()));
            }
        }

        String deleteMessage = USER_DELETED_ID;
        userRepository.deleteById(id);
        logService.addLog(String.format(deleteMessage, id), "WARNING", actorName);
        LOGGER.info(String.format(deleteMessage, id));
        return String.format(deleteMessage, id);
    }

    public String deleteByName(String name, String actorName) {
        if (name.equals(actorName)) {
            LOGGER.error(USER_DELETE_HIMSELF);
            throw new RuntimeException(USER_DELETE_HIMSELF);
        }
        User userToDelete = userRepository.findUserByName(name);
        User actor = userRepository.findUserByName(actorName);
        if (userToDelete == null) {
            LOGGER.error(String.format(USER_NOT_FOUND_NAME, name));
            throw new RuntimeException(String.format(USER_NOT_FOUND_NAME, name));
        }
        if (logService.existLogByActorId(userToDelete)) {
            LOGGER.error(String.format(USER_REFERENCED, name));
            throw new RuntimeException(String.format(USER_REFERENCED, userToDelete.getName()));
        }
        if (actor == null) {
            LOGGER.error(String.format(USER_NOT_FOUND_NAME, actorName));
            throw new RuntimeException(String.format(USER_NOT_FOUND_NAME, actorName));
        }

        String deleteMessage = USER_DELETED_NAME;
        userRepository.deleteById(userToDelete.getId());
        logService.addLog(String.format(deleteMessage, name), "WARNING", actorName);
        LOGGER.info(String.format(deleteMessage, name));
        return String.format(deleteMessage, name);
    }

    public String deleteAll() {
        if (!logRepository.findAll().isEmpty()) {
            LOGGER.warn(USERS_REFERENCED);
            throw new RuntimeException(USERS_REFERENCED);
        }
        userRepository.deleteAll();
        LOGGER.info(ALL_USERS_DELETED);
        return ALL_USERS_DELETED;
    }

    private void saveUser(User user, String actor) {
        userRepository.save(user);
        String bmi = bmiService.getBmiMessage(user.getBirthdate(), user.getWeight(), user.getHeight());
        logService.addLog(String.format(USER_CREATED + "%s", user.getName(), bmi),
                "INFO", actor);
        LOGGER.info(String.format(USER_CREATED + bmiService.getBmiMessage(
                user.getBirthdate(), user.getWeight(), user.getHeight()),
                user.getName()));
    }
}
