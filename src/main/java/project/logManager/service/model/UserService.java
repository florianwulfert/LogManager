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
            throw new UserNotFoundException(actorName);
        }
        if (id.equals(actor.getId())) {
            LOGGER.error("User cannot delete himself!");
            throw new RuntimeException("User cannot delete himself!");
        }
        if (user.isEmpty()) {
            LOGGER.error(String.format("User with the ID %s not found.", id));
            throw new RuntimeException(String.format("User with the ID %s not found.", id));
        } else {
            if (logService.existLogByActorId(user.get())) {
                LOGGER.error(String.format("User %s cannot be deleted because he is referenced in another table!",
                        user.get().getName()));
                throw new RuntimeException(String.format("User %s cannot be deleted because he is " +
                        "referenced in another table!", user.get().getName()));
            }
        }

        String deleteMessage = "User with the ID %s was deleted.";
        userRepository.deleteById(id);
        logService.addLog(String.format(deleteMessage, id), "WARNING", actorName);
        LOGGER.info(String.format(deleteMessage, id));
        return String.format(deleteMessage, id);
    }

    public String deleteByName(String name, String actorName) {
        if (name.equals(actorName)) {
            LOGGER.error("User cannot delete himself!");
            throw new RuntimeException("User cannot delete himself!");
        }
        User userToDelete = userRepository.findUserByName(name);
        User actor = userRepository.findUserByName(actorName);
        if (userToDelete == null) {
            LOGGER.error(String.format("User named %s not found!", name));
            throw new RuntimeException(String.format("User named %s not found!", name));
        }
        if (logService.existLogByActorId(userToDelete)) {
            LOGGER.error(String.format("User %s cannot be deleted because he is referenced in another table!", name));
            throw new RuntimeException(String.format("User %s cannot be deleted because he is " +
                    "referenced in another table!", userToDelete.getName()));
        }
        if (actor == null) {
            LOGGER.error(String.format("User named %s not found!", actorName));
            throw new RuntimeException(String.format("User named %s not found!", actorName));
        }

        String deleteMessage = "User named %s was deleted";
        userRepository.deleteById(userToDelete.getId());
        logService.addLog(String.format(deleteMessage, name), "WARNING", actorName);
        LOGGER.info(String.format(deleteMessage, name));
        return String.format(deleteMessage, name);
    }

    public String deleteAll() {
        if (!logRepository.findAll().isEmpty()) {
            LOGGER.warn("User %s cannot be deleted because he is referenced in another table!");
            throw new RuntimeException("User %s cannot be deleted because he is referenced in another table!");
        }
        userRepository.deleteAll();
        LOGGER.info("All users were deleted from database!");
        return "All users were deleted from database!";
    }

    public void saveUser(User user, String actor) {
        userRepository.save(user);
        String bmi = bmiService.getBmiMessage(user.getBirthdate(), user.getWeight(), user.getHeight());
        logService.addLog(String.format("User %s was created. %s", user.getName(), bmi),
                "INFO", actor);
        LOGGER.info(String.format("User %s was created. " +
                        bmiService.getBmiMessage(user.getBirthdate(), user.getWeight(), user.getHeight()),
                user.getName()));
    }
}
