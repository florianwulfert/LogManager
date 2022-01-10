package project.logManager.service.validation;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import project.logManager.common.enums.UserFarbenEnum;
import project.logManager.exception.ErsterUserUngleichActorException;
import project.logManager.model.entity.User;
import project.logManager.model.repository.UserRepository;
import project.logManager.service.model.LogService;
import project.logManager.service.model.UserService;

import java.util.List;

@RequiredArgsConstructor
public class UserValidationService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final LogService logService;

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    public boolean validateFarbenEnum(String userFarben) {
        for (UserFarbenEnum farbenEnum : UserFarbenEnum.values()) {
            if (userFarben.equals(farbenEnum.getFarbe())) {
                return true;
            }
        }
        return false;
    }

    public void handleFarbeNichtZugelassen(String lieblingsFarbe) {
        LOGGER.error("Die übergebene Farbe '{}' ist nicht zugelassen!", lieblingsFarbe);
        throw new IllegalArgumentException("Farbe falsch! Wählen Sie eine der folgenden Farben: " +
                "blau, rot, orange, gelb, schwarz");
    }

    public void checkIfUsersListIsEmpty(String actor, User user) {
        try {
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
        } catch (ErsterUserUngleichActorException ex) {
            handleErsterUserUngleichActor(actor, user, ex);
        }
    }

    public User checkIfActorExists(String actor) {
        try {
            User activeUser = userRepository.findUserByName(actor);
            if (activeUser == null) {
                LOGGER.error("Actor ist null!");
                throw new RuntimeException(String.format("User %s nicht gefunden", actor));
            }
            return activeUser;
        } catch (RuntimeException rex) {
            handleUserKonnteNichtAngelegtWerden(actor, rex);
        }
        return null;
    }

    public void checkIfUserToPostExists(String name) {
        try {
            if (userRepository.findUserByName(name) != null) {
                LOGGER.warn(String.format("User %s bereits vorhanden", name));
                throw new RuntimeException(String.format("User %s bereits vorhanden", name));
            }
        } catch (RuntimeException rex) {
            throw new RuntimeException(rex.getMessage());
        }
    }

    private void handleUserKonnteNichtAngelegtWerden(String actor, RuntimeException ex) {
        LOGGER.error("Der User konnte nicht angelegt werden");
        logService.addLog("Der User konnte nicht angelegt werden", "ERROR", actor);
        throw new RuntimeException(ex.getMessage());
    }

    private void handleErsterUserUngleichActor(String actor, User user, ErsterUserUngleichActorException er) {
        try {
            logService.addLog("Der User konnte nicht angelegt werden", "ERROR", actor);
            throw new  ErsterUserUngleichActorException(actor, user.getName());
        } catch (RuntimeException rex) {
            throw new RuntimeException(er.getMessage());
        }
    }


}
