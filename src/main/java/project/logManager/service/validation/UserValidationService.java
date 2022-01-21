package project.logManager.service.validation;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import project.logManager.common.enums.UserColorEnum;
import project.logManager.exception.FirstUserUnequalActorException;
import project.logManager.model.entity.User;
import project.logManager.model.repository.UserRepository;
import project.logManager.service.model.LogService;
import project.logManager.service.model.UserService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserValidationService {

    private final UserRepository userRepository;
    private final LogService logService;

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    public void validateFarbenEnum(String userFarben) {
        for (UserColorEnum farbenEnum : UserColorEnum.values()) {
            if (userFarben.equals(farbenEnum.getColor())) {
                return;
            }
        }
        LOGGER.error("Die übergebene Farbe '{}' ist nicht zugelassen!", userFarben);
        throw new IllegalArgumentException("Farbe falsch! Wählen Sie eine der folgenden Farben: " +
                "blau, rot, orange, gelb, schwarz");
    }

    public boolean checkIfUsersListIsEmpty(String actor, User user) {
        try {
            List<User> users = userRepository.findAll();
            if (users.isEmpty()) {
                if (!user.getName().equals(actor)) {
                    LOGGER.warn("User kann nicht angelegt werden, da noch keine User in der " +
                            "Datenbank angelegt sind. Erster User muss sich selbst anlegen! " +
                            user.getName() + " ungleich " + actor);
                    throw new FirstUserUnequalActorException(actor, user.getName());
                }
            }
            return true;
        } catch (FirstUserUnequalActorException ex) {
            handleErsterUserUngleichActor(actor, ex);
            return false;
        }
    }

    private void handleErsterUserUngleichActor(String actor, FirstUserUnequalActorException er) {
        try {
            logService.addLog("Der User konnte nicht angelegt werden", "ERROR", actor);
        } catch (RuntimeException rex) {
            throw new RuntimeException(er.getMessage());
        }
    }

    public User checkIfActorExists(String actor) {
        try {
            User activeUser = userRepository.findUserByName(actor);
            if (activeUser == null) {
                LOGGER.error("Actor is null!");
                throw new RuntimeException(String.format("User %s not found.", actor));
            }
            return activeUser;
        } catch (RuntimeException rex) {
            throw new RuntimeException(handleUserCouldNotBeCreated(actor, rex));
        }
    }

    private String handleUserCouldNotBeCreated(String actor, RuntimeException ex) {
        LOGGER.error("User could not be created.");
        logService.addLog("User could not be created.", "ERROR", actor);
        return ex.getMessage();
    }

    public void checkIfUserToPostExists(String name) {
        try {
            if (userRepository.findUserByName(name) != null) {
                LOGGER.warn(String.format("User %s already exists.", name));
                throw new RuntimeException(String.format("User %s already exists.", name));
            }
        } catch (RuntimeException rex) {
            throw new RuntimeException(rex.getMessage());
        }
    }


}
