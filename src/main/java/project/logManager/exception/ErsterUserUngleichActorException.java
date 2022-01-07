package project.logManager.exception;

public class ErsterUserUngleichActorException extends RuntimeException {

    public ErsterUserUngleichActorException(String actor, String name) {
        super("User kann nicht angelegt werden, da noch keine User in der " +
                "Datenbank angelegt sind. Erster User muss sich selbst anlegen! " +
                name + " ungleich " + actor);
    }
}
