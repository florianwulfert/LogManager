package project.logManager.common.message;

public class ErrorMessages {

    public static final String USER_TOO_YOUNG = "User is too young for an exact definition of the BMI.";
    public static final String COULD_NOT_CALCULATE = "BMI could not be calculated.";
    public static final String USER_NOT_IDENTIFIED = "User %s not identified!";
    public static final String USER_DELETE_HIMSELF = "User cannot delete himself!";
    public static final String USER_NOT_FOUND_ID = "User with the ID %s not found.";
    public static final String USER_REFERENCED = "User %s cannot be deleted because he is referenced in another table!";
    public static final String USER_NOT_FOUND_NAME = "User named %s not found!";
    public static final String USERS_REFERENCED = "Users cannot be deleted because they are referenced in another table!";
    public static final String SEVERITY_NOT_REGISTERED = "Given severity {} is not registered!";
    public static final String NO_ENTRIES_FOUND = "No entries found!";
    public static final String COLOR_ILLEGAL = "Given color {} is illegal!";
    public static final String COLOR_ILLEGAL_PLUS_CHOICE = "Color illegal! Choose from the following options: " +
            "blue, red, orange, yellow, black";
    public static final String NO_USERS_YET = "User cannot be created because there are no users in the database yet. " +
            "First user has to create himself! ";
    public static final String USER_NOT_CREATED = "User could not be created.";
    public static final String ACTOR_IS_NULL = "Actor is null!";
    public static final String USER_EXISTS = "User %s already exists.";
    public static final String HEIGHT_NOT_PRESENT = "Required double parameter 'height' is not present";
    public static final String WEIGHT_NOT_PRESENT = "Required double parameter 'weight' is not present";
    public static final String BIRTHDATE_NOT_PRESENT = "Required LocalDate parameter 'birthdate' is not present";
    public static final String MESSAGE_NOT_PRESENT = "Required String parameter 'message' is not present";
    public static final String SEVERITY_NOT_PRESENT = "Required String parameter 'severity' is not present";
    public static final String ID_NOT_PRESENT = "Required Integer parameter 'id' is not present";
    public static final String NAME_NOT_PRESENT = "Required String parameter 'name' is not present";
    public static final String FAVOURITE_COLOR_NOT_PRESENT = "Required String parameter 'favouriteColor' is not present";
    public static final String ACTOR_NOT_PRESENT = "Required String parameter 'actor' is not present";
    public static final String NAME_USER_NOT_PRESENT = "Required String parameter 'nameUser' is not present";
    public static final String SEVERITY_NOT_REGISTERED_CHOICE = "Severity %s not registered. Please choose one of the following options:" +
            " TRACE, DEBUG, INFO, WARNING, ERROR, FATAL";
    public static final String INFINITE_OR_NAN = "Infinite or NaN";
    public static final String ID_NOT_FOUND =  "ID %s not found.";
}
