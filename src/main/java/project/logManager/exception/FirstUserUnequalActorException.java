package project.logManager.exception;

import project.logManager.common.message.ErrorMessages;

public class FirstUserUnequalActorException extends RuntimeException {

    public FirstUserUnequalActorException(String actor, String name) {
        super(ErrorMessages.NO_USERS_YET + name + " unequal " + actor);
    }
}
