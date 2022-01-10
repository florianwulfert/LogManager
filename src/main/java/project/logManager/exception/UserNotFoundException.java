package project.logManager.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String userName) {
        super(String.format("User %s konnte nicht identifiziert werden!", userName));
    }
}
