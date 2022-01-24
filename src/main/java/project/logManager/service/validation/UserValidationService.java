package project.logManager.service.validation;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import project.logManager.common.enums.UserFarbenEnum;
import project.logManager.exception.ErsterUserUngleichActorException;
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
        for (UserFarbenEnum farbenEnum : UserFarbenEnum.values()) {
            if (userFarben.equals(farbenEnum.getFarbe())) {
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
                    throw new ErsterUserUngleichActorException(actor, user.getName());
                }
                return true;
            } return false;
        } catch (ErsterUserUngleichActorException ex) {
            handleErsterUserUngleichActor(actor, ex);
            return false;
        }
    }

    private void handleErsterUserUngleichActor(String actor, ErsterUserUngleichActorException er) {
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
                LOGGER.error("Actor ist null!");
                throw new RuntimeException(String.format("User %s nicht gefunden", actor));
            }
            return activeUser;
        } catch (RuntimeException rex) {
            throw new RuntimeException(handleUserKonnteNichtAngelegtWerden(actor, rex));
        }
    }

    private String handleUserKonnteNichtAngelegtWerden(String actor, RuntimeException ex) {
        LOGGER.error("Der User konnte nicht angelegt werden");
        logService.addLog("Der User konnte nicht angelegt werden", "ERROR", actor);
        return ex.getMessage();
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


}
