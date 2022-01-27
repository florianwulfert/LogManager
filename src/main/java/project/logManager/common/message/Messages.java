package project.logManager.common.message;

public class Messages {
    public static final String USER_TOO_YOUNG = "User is too young for an exact definition of the BMI.";
    public static final String BMI_MESSAGE = "User has a BMI of %s and therewith he has";
    public static final String NORMAL_WEIGHT = " normal weight.";
    public static final String UNDERWEIGHT = " underweight.";
    public static final String OVERWEIGHT = " overweight.";
    public static final String COULD_NOT_CALCULATE = "BMI could not be calculated.";
    public static final String USER_NOT_IDENTIFIED = "User %s not identified!";
    public static final String USER_DELETE_HIMSELF = "User cannot delete himself!";
    public static final String USER_NOT_FOUND_ID = "User with the ID %s not found.";
    public static final String USER_REFERENCED = "User %s cannot be deleted because he is referenced in another table!";
    public static final String USER_DELETED_ID = "User with the ID %s was deleted.";
    public static final String USER_NOT_FOUND_NAME = "User named %s not found!";
    public static final String USER_DELETED_NAME = "User named %s was deleted.";
    public static final String USERS_REFERENCED = "Users cannot be deleted because they are referenced in another table!";
    public static final String ALL_USERS_DELETED = "All users were deleted from database!";
    public static final String USER_CREATED = "User %s was created. ";
    public static final String SEVERITY_NOT_REGISTERED = "Given severity {} is not registered!";
    public static final String MESSAGE_SAVED = "Message \"%s\" saved as %s!";
    public static final String ENTRY_DELETED_ID = "Entry with the ID %s was deleted from database.";
    public static final String NO_ENTRIES_FOUND = "No entries found!";
    public static final String ALL_LOGS_DELETED = "All logs were deleted from database!";
    public static final String KATZE_TO_HUND = "Katze was translated to Hund!";
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
    public static final String USER_UNDERWEIGHT = "User has a BMI of 18.3 and therewith he has underweight.";
    public static final String USER_OVERWEIGHT = "User has a BMI of 28.74 and therewith he has overweight.";
    public static final String USER_NORMAL_WEIGHT = "User has a BMI of 22.11 and therewith he has normal weight.";
    public static final String ACTOR_NOT_IDENTIFIED = "User ActorNichtVorhanden not identified!";
    public static final String HUGO_CREATED = "User Hugo was created. User has a BMI of 24.07 and therewith he has normal weight.";
    public static final String PETRA_CREATED = "User Petra was created. User has a BMI of 24.07 and therewith he has normal weight.";
    public static final String ACTORNAME_NOT_FOUND = "User named ActorName not found!";
    public static final String ACTOR_NOT_PRESENT = "Required String parameter 'actor' is not present";
    public static final String BAD_REQUEST_ERROR_MESSAGE_DATE = "Required path variable was not found or request param has wrong format! " +
            "Failed to convert value of type 'java.lang.String' to required type 'java.time.LocalDate'; " +
            "nested exception is org.springframework.core.convert.ConversionFailedException: " +
            "Failed to convert from type [java.lang.String] to type " +
            "[@org.springframework.web.bind.annotation.RequestParam @org.springframework.format." +
            "annotation.DateTimeFormat java.time.LocalDate] for value 'hallo'; " +
            "nested exception is java.lang.IllegalArgumentException: Parse attempt failed for value [hallo]";
    public static final String BAD_REQUEST_ERROR_MESSAGE_WEIGHT = "Required path variable was not found or request param has wrong format! " +
            "Failed to convert value of type 'java.lang.String' to required type 'double'; " +
            "nested exception is java.lang.NumberFormatException: For input string: \"hi\"";
    public static final String BAD_REQUEST_ERROR_MESSAGE_HEIGHT = "Required path variable was not found or request param has wrong format! " +
            "Failed to convert value of type 'java.lang.String' to required type 'double'; " +
            "nested exception is java.lang.NumberFormatException: For input string: \"hi\"";
    public static final String TORSTEN_EXISTS = "User Torsten already exists.";
    public static final String NAME_NOT_PRESENT = "Required String parameter 'name' is not present";
    public static final String FAVOURITE_COLOR_NOT_PRESENT = "Required String parameter 'favouriteColor' is not present";
    public static final String PETRA_TORSTEN_HANS = "[{\"id\":1,\"name\":\"Petra\",\"birthdate\":\"1999-12-13\"," +
            "\"weight\":65.0,\"height\":1.6,\"favouriteColor\":\"Red\",\"bmi\":25.39}" +
            ",{\"id\":2,\"name\":\"Torsten\",\"birthdate\":\"1985-12-05\",\"weight\":61.3,\"height\":1.83," +
            "\"favouriteColor\":\"Blue\",\"bmi\":18.3}," +
            "{\"id\":3,\"name\":\"Hans\",\"birthdate\":\"1993-02-03\",\"weight\":75.7,\"height\":1.85," +
            "\"favouriteColor\":\"Red\",\"bmi\":22.11}]";
    public static final String PETRA = "{\"id\":1,\"name\":\"Petra\",\"birthdate\":\"1999-12-13\"," +
            "\"weight\":65.0,\"height\":1.6,\"favouriteColor\":\"Red\",\"bmi\":25.39}";
    public static final String ID_NOT_PRESENT = "Required Integer parameter 'id' is not present";
    public static final String PETRA_DELETED = "User named Petra was deleted.";
    public static final String USER_TO_DELETE_NOT_FOUND = "User named UserToDeleteNichtBekannt not found!";
    public static final String USER_TO_DELETE_NOT_PRESENT = "Required path variable was not found or request param has wrong format! "
            + "Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; "
            + "nested exception is java.lang.NumberFormatException: For input string: \"name\"";
    public static final String PETRA_REFERENCED = "User Petra cannot be deleted because he is referenced in another table!";
    public static final String PAUL_NOT_IDENTIFIED = "User Paul not identified!";
    public static final String ID_1_DELETED = "User with the ID 1 was deleted.";
    public static final String ID_8_NOT_FOUND = "User with the ID 8 not found.";
    public static final String MESSAGE_NOT_PRESENT = "Required String parameter 'message' is not present";
    public static final String SEVERITY_NOT_PRESENT = "Required String parameter 'severity' is not present";
    public static final String HANS_NOT_FOUND = "User named Hans not found!";
    public static final String NAME_USER_NOT_PRESENT = "Required String parameter 'nameUser' is not present";
    public static final String SEVERITY_HI_NOT_REGISTERED = "Severity hi not registered. Please choose one of the following options: TRACE, DEBUG, INFO, WARNING, ERROR, FATAL";
    public static final String HUND_SAVED = "Message \"Hund\" saved as INFO!";
    public static final String LOG_EXAMPLE = "[{\"severity\":\"INFO\",\"message\":\"Test\",\"timestamp\":\"2000-12-12T12:12:12\"}]";
    public static final String ID_FOR_LOGS_HAS_WRONG_FORMAT = "Required path variable was not found or request param has wrong format! " +
            "Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; " +
            "nested exception is java.lang.NumberFormatException: For input string: \"hallo\"";
    public static final String ID_2_DELETED = "Entry with the ID 2 was deleted from database.";
    public static final String ID_NOT_EXISTS = "No class project.logManager.model.entity.Log entity with id 20 exists!";
    public static final String ENTRIES_DELETED = "Entries with the ID(s) 1, 2 were deleted from database.";


}
