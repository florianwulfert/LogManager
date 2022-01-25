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
        LOGGER.error("Given color {} is illegal!", userFarben);
        throw new IllegalArgumentException("Color illegal! Choose from the following options: " +
                "blue, red, orange, yellow, black");
    }

    public boolean checkIfUsersListIsEmpty(String actor, User user) {
        try {
            List<User> users = userRepository.findAll();
            if (users.isEmpty()) {
                if (!user.getName().equals(actor)) {
                    LOGGER.warn("User cannot be created because there are no users in the database yet. " +
                            "First user has to create himself! " +
                            user.getName() + " ungleich " + actor);
                    throw new FirstUserUnequalActorException(actor, user.getName());
                }
                return true;
            } return false;
        } catch (FirstUserUnequalActorException ex) {
            handleErsterUserUngleichActor(actor, ex);
            return false;
        }
    }

    private void handleErsterUserUngleichActor(String actor, FirstUserUnequalActorException er) {
        try {
            logService.addLog("User could not be created.", "ERROR", actor);
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
            throw new RuntimeException(handleUserKonnteNichtAngelegtWerden(actor, rex));
        }
    }

    private String handleUserKonnteNichtAngelegtWerden(String actor, RuntimeException ex) {
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
