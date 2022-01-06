package project.logManager.exception;

/**
* @author - EugenFriesen
* 14.02.2021
**/

public class SeverityNotFoundException extends IllegalArgumentException {

    public SeverityNotFoundException(String severity) {
        super("Severity " + severity + " ist nicht zugelassen. Bitte wählen Sie eins der folgenden severities aus:" +
                " TRACE, DEBUG, INFO, WARNING, ERROR, FATAL");
    }
}
