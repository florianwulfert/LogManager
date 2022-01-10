package project.logManager.service.validation;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import project.logManager.common.enums.UserFarbenEnum;
import project.logManager.exception.ErsterUserUngleichActorException;
import project.logManager.model.entity.User;
import project.logManager.model.repository.UserRepository;
import project.logManager.service.model.UserService;

import java.util.List;

@RequiredArgsConstructor
public class UserValidationService {

    private final UserService userService;
    private final UserRepository userRepository;

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    public boolean validateFarbenEnum(String userFarben) {
        for (UserFarbenEnum farbenEnum : UserFarbenEnum.values()) {
            if (userFarben.equals(farbenEnum.getFarbe())) {
                return true;
            }
        }
        return false;
    }

    public void checkIfUsersListIsEmpty(String actor, User user) {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            if (user.getName().equals(actor)) {
                userService.saveUser(user, user.getName());
            } else {
                LOGGER.warn("User kann nicht angelegt werden, da noch keine User in der " +
                        "Datenbank angelegt sind. Erster User muss sich selbst anlegen! " +
                        user.getName() + " ungleich " + actor);
                throw new ErsterUserUngleichActorException(actor, user.getName());
            }
        }
    }

    public User checkIfActorExists(String actor) {
        User activeUser = userRepository.findUserByName(actor);
        if (activeUser == null) {
            LOGGER.error("Actor ist null!");
            throw new RuntimeException(String.format("User %s nicht gefunden", actor));
        }
        return activeUser;
    }

    public void checkIfUserToPostExists(String name) {
        if (userRepository.findUserByName(name) != null) {
            LOGGER.warn(String.format("User %s bereits vorhanden", name));
            throw new RuntimeException(String.format("User %s bereits vorhanden", name));
        }
    }
}
