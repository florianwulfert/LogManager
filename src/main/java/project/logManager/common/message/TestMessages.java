package project.logManager.common.message;

public class TestMessages {

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
    public static final String PETRA_TORSTEN_HANS = "[{\"id\":1,\"name\":\"Petra\",\"birthdate\":\"1999-12-13\"," +
            "\"weight\":65.0,\"height\":1.6,\"favouriteColor\":\"Red\",\"bmi\":25.39}" +
            ",{\"id\":2,\"name\":\"Torsten\",\"birthdate\":\"1985-12-05\",\"weight\":61.3,\"height\":1.83," +
            "\"favouriteColor\":\"Blue\",\"bmi\":18.3}," +
            "{\"id\":3,\"name\":\"Hans\",\"birthdate\":\"1993-02-03\",\"weight\":75.7,\"height\":1.85," +
            "\"favouriteColor\":\"Red\",\"bmi\":22.11}]";
    public static final String PETRA = "{\"id\":1,\"name\":\"Petra\",\"birthdate\":\"1999-12-13\"," +
            "\"weight\":65.0,\"height\":1.6,\"favouriteColor\":\"Red\",\"bmi\":25.39}";
    public static final String USER_TO_DELETE_NOT_PRESENT = "Required path variable was not found or request param has wrong format! "
            + "Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; "
            + "nested exception is java.lang.NumberFormatException: For input string: \"name\"";
    public static final String LOG_EXAMPLE = "[{\"severity\":\"INFO\",\"message\":\"Test\",\"timestamp\":\"2000-12-12T12:12:12\"}]";
    public static final String ID_FOR_LOGS_HAS_WRONG_FORMAT = "Required path variable was not found or request param has wrong format! " +
            "Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; " +
            "nested exception is java.lang.NumberFormatException: For input string: \"hallo\"";
    public static final String ID_NOT_EXISTS = "No class project.logManager.model.entity.Log entity with id 20 exists!";
    public static final String ENTRIES_DELETED = "Entries with the ID(s) 1, 2 were deleted from database.";
}
