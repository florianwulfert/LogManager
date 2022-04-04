package project.logManager;

public class TestMessages {

  public static final String PETRA_TORSTEN_HANS =
      "{\"result\":[{\"id\":1,\"name\":\"Petra\",\"birthdate\":\"1999-12-13\","
          + "\"weight\":65.0,\"height\":1.6,\"favouriteColor\":\"Red\",\"bmi\":25.39}"
          + ",{\"id\":2,\"name\":\"Torsten\",\"birthdate\":\"1985-12-05\",\"weight\":61.3,\"height\":1.83,"
          + "\"favouriteColor\":\"Blue\",\"bmi\":18.3},"
          + "{\"id\":3,\"name\":\"Hans\",\"birthdate\":\"1993-02-03\",\"weight\":75.7,\"height\":1.85,"
          + "\"favouriteColor\":\"Red\",\"bmi\":22.11}],\"returnMessage\":null}";
  public static final String PETRA = "{\"id\":1,\"name\":\"Petra\",\"birthdate\":\"1999-12-13\","
      + "\"weight\":65.0,\"height\":1.6,\"favouriteColor\":\"Red\",\"bmi\":25.39}";
  public static final String USER_TO_DELETE_NOT_PRESENT = "Required path variable was not found or request param has wrong format! "
      + "Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; "
      + "nested exception is java.lang.NumberFormatException: For input string: \"name\"";
  public static final String LOG_EXAMPLE = "[{\"severity\":\"INFO\",\"message\":\"Test\",\"timestamp\":\"2000-12-12T12:12:12\",\"user\":null}]";
  public static final String ID_FOR_LOGS_HAS_WRONG_FORMAT = "Required path variable was not found or request param has wrong format! "
      + "Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; "
      + "nested exception is java.lang.NumberFormatException: For input string: \"hallo\"";
  public static final String ID_NOT_EXISTS = "No class project.logManager.model.entity.Log entity with id 20 exists!";
  public static final String ENTRIES_DELETED = "Entries with the ID(s) 1, 2 were deleted from database.";
  public static final String ACTOR_NOT_PRESENT = "Required String parameter 'actor' is not present";
  public static final String HAYA = "{\"id\":16,\"titel\":\"haya\",\"erscheinungsjahr\":1998}";
  public static final String PETRA_BOOK = "{\"id\":25,\"titel\":\"petra\",\"erscheinungsjahr\":1999}";
  public static final String ID_FOR_BOOK_HAS_WRONG_FORMAT = "Required path variable was not found or request param has wrong format! "
      + "Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; "
      + "nested exception is java.lang.NumberFormatException: For input string: \"kevin\"";
  public static final String USER_CREATED_MESSAGE =
      "{\"result\":[{\"id\":1,\"name\":\"Petra\",\"birthdate\":\"1999-12-13\","
          + "\"weight\":65.0,\"height\":1.6,\"favouriteColor\":\"Red\",\"bmi\":25.39}"
          + ",{\"id\":2,\"name\":\"Torsten\",\"birthdate\":\"1985-12-05\",\"weight\":61.3,\"height\":1.83,"
          + "\"favouriteColor\":\"Blue\",\"bmi\":18.3},"
          + "{\"id\":3,\"name\":\"Hans\",\"birthdate\":\"1993-02-03\",\"weight\":75.7,\"height\":1.85,"
          + "\"favouriteColor\":\"Red\",\"bmi\":22.11},{\"id\":4,\"name\":\"Hugo\",\"birthdate\":\"1999-12-13\",\"weight\":78.0,\"height\":1.8,\"favouriteColor\":\"red\",\"bmi\":24.07}],"
          +
          "\"returnMessage\":\"User has a BMI of 24.07 and therewith he has normal weight.\"}";
  public static final String EMPTY_LIST =
      "{\"result\":[],\"returnMessage\":\"All users were deleted from database!\"}";
  public static final String USER_DELETED_BY_ID =
      "{\"result\":[{\"id\":2,\"name\":\"Torsten\",\"birthdate\":\"1985-12-05\",\"weight\":61.3,\"height\":1.83,"
          + "\"favouriteColor\":\"Blue\",\"bmi\":18.3},"
          + "{\"id\":3,\"name\":\"Hans\",\"birthdate\":\"1993-02-03\",\"weight\":75.7,\"height\":1.85,"
          + "\"favouriteColor\":\"Red\",\"bmi\":22.11}],\"returnMessage\":null}";
  public static final String USER_PETRA_DELETED_BY_NAME =
      "{\"result\":[{\"id\":2,\"name\":\"Torsten\",\"birthdate\":\"1985-12-05\",\"weight\":61.3,\"height\":1.83,"
          + "\"favouriteColor\":\"Blue\",\"bmi\":18.3},"
          + "{\"id\":3,\"name\":\"Hans\",\"birthdate\":\"1993-02-03\",\"weight\":75.7,\"height\":1.85,"
          + "\"favouriteColor\":\"Red\",\"bmi\":22.11}],\"returnMessage\":\"User named Petra was deleted.\"}";
}
