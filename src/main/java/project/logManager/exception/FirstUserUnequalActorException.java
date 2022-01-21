package project.logManager.exception;

public class FirstUserUnequalActorException extends RuntimeException {

    public FirstUserUnequalActorException(String actor, String name) {
        super("User cannot be created because there are no user in the database yet. First user has to create himself! " +
                name + " unequal " + actor);
    }
}
