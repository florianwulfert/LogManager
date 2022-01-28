package project.logManager.service.validation;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import project.logManager.common.enums.UserColorEnum;
import project.logManager.common.message.ErrorMessages;
import project.logManager.exception.FirstUserUnequalActorException;
import project.logManager.exception.UserNotFoundException;
import project.logManager.model.entity.User;
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

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    public void validateFarbenEnum(String userFarben) {
        for (UserColorEnum farbenEnum : UserColorEnum.values()) {
            if (userFarben.equals(farbenEnum.getColor())) {
                return;
            }
        }
        LOGGER.error(ErrorMessages.COLOR_ILLEGAL, userFarben);
        throw new IllegalArgumentException(ErrorMessages.COLOR_ILLEGAL_PLUS_CHOICE);
    }

    public boolean checkIfUsersListIsEmpty(String actor, User user, boolean onCreate) {
        try {
            List<User> users = userRepository.findAll();
            if (users.isEmpty()) {
                if (!user.getName().equals(actor)) {
                    if (onCreate) {
                        LOGGER.warn(ErrorMessages.NO_USERS_YET +
                                user.getName() + " ungleich " + actor);
                        throw new FirstUserUnequalActorException(actor, user.getName());
                    } else {
                        LOGGER.error(String.format(ErrorMessages.USER_NOT_FOUND_ID, user.getId()));
                        throw new RuntimeException(String.format(ErrorMessages.USER_NOT_FOUND_ID, user.getId()));
                    }
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
            logService.addLog(ErrorMessages.USER_NOT_CREATED, "ERROR", actor);
        } catch (RuntimeException rex) {
            throw new RuntimeException(er.getMessage());
        }
    }

    public User checkIfActorExists(String actor, boolean isCreate) {
        try {
            User activeUser = userRepository.findUserByName(actor);
            if (activeUser == null) {
                LOGGER.warn(String.format(ErrorMessages.USER_NOT_IDENTIFIED, actor));
                throw new UserNotFoundException(actor);
            } return activeUser;
        } catch (RuntimeException rex) {
            if (isCreate) {
                throw new RuntimeException(handleUserKonnteNichtAngelegtWerden(actor, rex));
            } else {
                throw new RuntimeException(rex.getMessage());
            }
        }
    }

    private String handleUserKonnteNichtAngelegtWerden(String actor, RuntimeException ex) {
        LOGGER.error(ErrorMessages.USER_NOT_CREATED);
        logService.addLog(ErrorMessages.USER_NOT_CREATED, "ERROR", actor);
        return ex.getMessage();
    }

    public void checkIfUserToPostExists(String name) {
        try {
            if (userRepository.findUserByName(name) != null) {
                LOGGER.warn(String.format(ErrorMessages.USER_EXISTS, name));
                throw new RuntimeException(String.format(ErrorMessages.USER_EXISTS, name));
            }
        } catch (RuntimeException rex) {
            throw new RuntimeException(rex.getMessage());
        }
    }

    public void checkIfUserToDeleteIdEqualsActorId(int userToDeleteId, int actorId) {
        if (userToDeleteId == actorId) {
            LOGGER.error(ErrorMessages.USER_DELETE_HIMSELF);
            throw new RuntimeException(ErrorMessages.USER_DELETE_HIMSELF);
        }
    }

    public User checkIfIdExists(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            LOGGER.info(String.format(ErrorMessages.ID_NOT_FOUND, id));
            throw new RuntimeException(String.format(ErrorMessages.ID_NOT_FOUND, id));
        } return user.get();
    }

    public void checkIfExistLogByUserToDelete(User userToDelete) {
        if (logService.existLogByUserToDelete(userToDelete)) {
            LOGGER.error(String.format(ErrorMessages.USER_REFERENCED, userToDelete.getName()));
            throw new RuntimeException(String.format(ErrorMessages.USER_REFERENCED, userToDelete.getName()));
        }
    }





}
