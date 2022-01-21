package project.logManager.exception;

/**
* @author - EugenFriesen
* 14.02.2021
**/

public class SeverityNotFoundException extends IllegalArgumentException {

    public SeverityNotFoundException(String severity) {
        super("Severity " + severity + " not registered. Please choose one of the following options:" +
                " TRACE, DEBUG, INFO, WARNING, ERROR, FATAL");
    }
}
