package seedu.duke;

public class ErrorMessages {
    //public enum ErrorTypes {
    //    INVALID_LIST_INDEX_DELETE,INVALID_LIST_INDEX_DELETE_STRING_DETECTED
    //}

    // can generalise error messages to all commands that requires index
    public static final String ERROR_INDEX_OUT_OF_BOUND = "Hi I need a valid index in the list so I can delete";
    public static final String ERROR_INVALID_INDEX_FORMAT = "Hi I did not receive a proper "
            + "integer that I can parse";
    public static final String ERROR_MISSING_PARAMETERS = "Hi 1 or more parameters are missing";
    public static final String ERROR_MISSING_VALUES = "Hi 1 or more parameters have missing values";
    public static final String ERROR_INVALID_DAY = "Hi please input a valid day\n"
            + "Accepted inputs are: 0000 - 2359";
    public static final String ERROR_INVALID_TIME = "Hi please input a valid time\n"
            + "Accepted inputs are:\n"
            + "monday\n"
            + "tuesday\n"
            + "wednesday\n"
            + "thursday\n"
            + "friday\n"
            + "saturday\n"
            + "sunday";
    public static final String ERROR_INVALID_MODE = "Hi please input a valid mode\n"
            + "Accepted inputs are: online or physical";

}
