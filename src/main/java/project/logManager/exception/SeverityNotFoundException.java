package project.logManager.exception;

/**
* @author - EugenFriesen
* 14.02.2021
**/

public class SeverityNotFoundException extends IllegalArgumentException {

    public SeverityNotFoundException(String severity) {
        super("Severity " + severity + " is not allowed. Please Choose one of these: TRACE, DEBUG, INFO, WARNING, ERROR, FATAL");
    }
}
