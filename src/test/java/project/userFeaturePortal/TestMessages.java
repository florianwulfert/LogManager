package project.userFeaturePortal;

public class TestMessages {

    public static final String PETRA_TORSTEN_HANS = "{\"result\":[{\"id\":1,\"name\":\"Petra\",\"birthdate\":\"1999-12-13\","
            + "\"weight\":65.0,\"height\":1.6,\"favouriteBookTitel\":null,\"bmi\":25.39}"
            + ",{\"id\":2,\"name\":\"Torsten\",\"birthdate\":\"1985-12-05\",\"weight\":61.3,\"height\":1.83,"
            + "\"favouriteBookTitel\":null,\"bmi\":18.3},"
            + "{\"id\":3,\"name\":\"Hans\",\"birthdate\":\"1993-02-03\",\"weight\":75.7,\"height\":1.85,"
            + "\"favouriteBookTitel\":null,\"bmi\":22.11}],\"returnMessage\":null}";
    public static final String PETRA = "{\"id\":1,\"name\":\"Petra\",\"birthdate\":\"1999-12-13\","
            + "\"weight\":65.0,\"height\":1.6,\"favouriteBook\":null,\"bmi\":25.39}";
    public static final String LOG_EXAMPLE = "[{\"id\":1,\"severity\":\"INFO\",\"message\":\"Test\",\"timestamp\":\"2000-12-12T12:12:12\",\"user\":null}]";
    public static final String ID_FOR_LOGS_HAS_WRONG_FORMAT = "Required path variable was not found or request param has wrong format! "
            + "Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; "
            + "nested exception is java.lang.NumberFormatException: For input string: \"hallo\"";
    public static final String ID_NOT_EXISTS = "No class project.userFeaturePortal.model.entity.Log entity with id 20 exists!";
    public static final String ENTRIES_DELETED = "Entries with the ID(s) 1, 2 were deleted from database.";
    public static final String ACTOR_NOT_PRESENT = "Required String parameter 'actor' is not present";
    public static final String TESTBOOK_CREATED = "{\"result\":[{\"id\":1,\"titel\":\"TestBook\",\"erscheinungsjahr\":1998},"
            + "{\"id\":2,\"titel\":\"petra\",\"erscheinungsjahr\":1989},"
            + "{\"id\":3,\"titel\":\"peter\",\"erscheinungsjahr\":2010},"
            + "{\"id\":4,\"titel\":\"lina\",\"erscheinungsjahr\":2009},"
            + "{\"id\":5,\"titel\":\"omar\",\"erscheinungsjahr\":2002},"
            + "{\"id\":6,\"titel\":\"paul\",\"erscheinungsjahr\":2002}," +
            "{\"id\":7,\"titel\":\"TestBook1\",\"erscheinungsjahr\":1998}]," +
            "\"returnMessage\":\"Book TestBook1 was created.\"}";
    public static final String ID_FOR_BOOK_HAS_WRONG_FORMAT = "Required path variable was not found or request param has wrong format! "
            + "Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; "
            + "nested exception is java.lang.NumberFormatException: For input string: \"kevin\"";
    public static final String USER_CREATED_MESSAGE = "{\"result\":[{\"id\":1,\"name\":\"Petra\",\"birthdate\":\"1999-12-13\","
            + "\"weight\":65.0,\"height\":1.6,\"favouriteBookTitel\":null,\"bmi\":25.39}"
            + ",{\"id\":2,\"name\":\"Torsten\",\"birthdate\":\"1985-12-05\",\"weight\":61.3,\"height\":1.83,"
            + "\"favouriteBookTitel\":null,\"bmi\":18.3},"
            + "{\"id\":3,\"name\":\"Hans\",\"birthdate\":\"1993-02-03\",\"weight\":75.7,\"height\":1.85,"
            + "\"favouriteBookTitel\":null,\"bmi\":22.11}," +
            "{\"id\":4,\"name\":\"Hugo\",\"birthdate\":\"1999-12-13\",\"weight\":78.0,\"height\":1.8,\"favouriteBookTitel\":null,\"bmi\":24.07}],"
            +
            "\"returnMessage\":\"User Hugo was created.\"}";
    public static final String EMPTY_LIST = "{\"result\":[],\"returnMessage\":\"All users were deleted from database!\"}";
    public static final String USER_DELETED_BY_ID = "{\"result\":[{\"id\":2,\"name\":\"Torsten\",\"birthdate\":\"1985-12-05\",\"weight\":61.3,\"height\":1.83,"
            + "\"favouriteBookTitel\":null,\"bmi\":18.3},"
            + "{\"id\":3,\"name\":\"Hans\",\"birthdate\":\"1993-02-03\",\"weight\":75.7,\"height\":1.85,"
            + "\"favouriteBookTitel\":null,\"bmi\":22.11}],\"returnMessage\":null}";
    public static final String USER_PETRA_DELETED_BY_NAME = "{\"result\":[{\"id\":2,\"name\":\"Torsten\",\"birthdate\":\"1985-12-05\",\"weight\":61.3,\"height\":1.83,"
            + "\"favouriteBookTitel\":null,\"bmi\":18.3},"
            + "{\"id\":3,\"name\":\"Hans\",\"birthdate\":\"1993-02-03\",\"weight\":75.7,\"height\":1.85,"
            + "\"favouriteBookTitel\":null,\"bmi\":22.11}],\"returnMessage\":\"User named Petra was deleted.\"}";
    public static final String BOOK_PETRA_DELETED_BY_ID = "{\"result\":[{\"id\":2,\"titel\":\"petra\",\"erscheinungsjahr\":1989},"
            + "{\"id\":3,\"titel\":\"peter\",\"erscheinungsjahr\":2010},"
            + "{\"id\":4,\"titel\":\"lina\",\"erscheinungsjahr\":2009},"
            + "{\"id\":5,\"titel\":\"omar\",\"erscheinungsjahr\":2002},"
            + "{\"id\":6,\"titel\":\"paul\",\"erscheinungsjahr\":2002}]," +
            "\"returnMessage\":\"Book with the ID 1 was deleted.\"}";

    public static final String BOOK_PETER_DELETED_BY_ID = "{\"result\":[{\"id\":1,\"titel\":\"TestBook\",\"erscheinungsjahr\":1998}," +
            "{\"id\":2,\"titel\":\"petra\",\"erscheinungsjahr\":1989},"
            + "{\"id\":4,\"titel\":\"lina\",\"erscheinungsjahr\":2009},"
            + "{\"id\":5,\"titel\":\"omar\",\"erscheinungsjahr\":2002},"
            + "{\"id\":6,\"titel\":\"paul\",\"erscheinungsjahr\":2002}]," +
            "\"returnMessage\":\"Book with the ID 3 was deleted.\"}";
    public static final String TESTBOOK = "{\"book\":{\"titel\":\"TestBook\",\"erscheinungsjahr\":1998}}";
    public static final String ALL_BOOKS_DELETED = "{\"result\":[],\"returnMessage\":\"All BOOKS were deleted from database!\"}";
    public static final String TESTBOOK_DELETED_BY_TITLE =
            "{\"result\":["
                    + "{\"id\":2,\"titel\":\"petra\",\"erscheinungsjahr\":1989}," +
                    "{\"id\":3,\"titel\":\"peter\",\"erscheinungsjahr\":2010},"
                    + "{\"id\":4,\"titel\":\"lina\",\"erscheinungsjahr\":2009},"
                    + "{\"id\":5,\"titel\":\"omar\",\"erscheinungsjahr\":2002},"
                    + "{\"id\":6,\"titel\":\"paul\",\"erscheinungsjahr\":2002}],"
                    + "\"returnMessage\":\"Book with the title TestBook was deleted.\"}";
    public static final String NO_BOOKS_FOUND =
            "{\"result\":[{\"id\":1,\"titel\":\"TestBook\",\"erscheinungsjahr\":1998},"
                    + "{\"id\":2,\"titel\":\"petra\",\"erscheinungsjahr\":1989},"
                    + "{\"id\":3,\"titel\":\"peter\",\"erscheinungsjahr\":2010},"
                    + "{\"id\":4,\"titel\":\"lina\",\"erscheinungsjahr\":2009},"
                    + "{\"id\":5,\"titel\":\"omar\",\"erscheinungsjahr\":2002},"
                    + "{\"id\":6,\"titel\":\"paul\",\"erscheinungsjahr\":2002}],"
                    + "\"returnMessage\":\"No books with the title hajer found.\"}";
    public static final String GET_BOOKS = "{\"result\":[{\"id\":1,\"titel\":\"TestBook\",\"erscheinungsjahr\":1998},"
            + "{\"id\":2,\"titel\":\"petra\",\"erscheinungsjahr\":1989},"
            + "{\"id\":3,\"titel\":\"peter\",\"erscheinungsjahr\":2010},"
            + "{\"id\":4,\"titel\":\"lina\",\"erscheinungsjahr\":2009},"
            + "{\"id\":5,\"titel\":\"omar\",\"erscheinungsjahr\":2002},"
            + "{\"id\":6,\"titel\":\"paul\",\"erscheinungsjahr\":2002}],"
            + "\"returnMessage\":null}";

    public static final String BOOK_HAYA_ADDED_BY_TORSTEN = "{\"favouriteBook\":\"haya\",\"returnMessage\":" +
            "\"Book haya added to user Torsten.\"}";
}