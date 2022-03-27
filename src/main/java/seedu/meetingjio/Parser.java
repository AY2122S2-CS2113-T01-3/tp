package seedu.meetingjio;

import seedu.meetingjio.commands.*;

import seedu.meetingjio.exceptions.InvalidDayException;
import seedu.meetingjio.exceptions.MissingValueException;
import seedu.meetingjio.exceptions.InvalidTimeException;
import seedu.meetingjio.exceptions.InvalidModeException;

import java.util.logging.Level;
import java.util.logging.Logger;

import static seedu.meetingjio.common.Messages.MESSAGE_HELP;
import static seedu.meetingjio.common.ErrorMessages.ERROR_INVALID_INDEX_FORMAT;
import static seedu.meetingjio.common.ErrorMessages.ERROR_MISSING_PARAMETERS;
import static seedu.meetingjio.common.ErrorMessages.ERROR_MISSING_VALUES;
import static seedu.meetingjio.common.ErrorMessages.ERROR_INVALID_DAY;
import static seedu.meetingjio.common.ErrorMessages.ERROR_INVALID_TIME;
import static seedu.meetingjio.common.ErrorMessages.ERROR_INVALID_MODE;
import static seedu.meetingjio.common.ErrorMessages.ERROR_INVALID_COMMAND;

public class Parser {
    private final String command;
    private final String arguments;
    public static Logger logger = Logger.getLogger(Parser.class.getName());

    private static final int NAME_INDEX = 0;
    private static final int TITLE_INDEX = 1;
    private static final int DAY_INDEX = 2;
    private static final int STARTTIME_INDEX = 3;
    private static final int ENDTIME_INDEX = 4;
    private static final int MODE_INDEX = 5;
    private static final String[] HEADINGS_ADD_LESSON = {"n/", "t/", "d/", "st/", "et/", "m/"};
    private static final String[] HEADINGS_ADD_MEETING = {"t/", "d/", "st/", "et/", "m/"};

    public Parser(String input) {
        this.command = getCommandFromInput(input);
        this.arguments = getArgumentsFromInput(input);
    }

    public Command parseCommand() {
        switch (command) {
        case AddLessonCommand.COMMAND_WORD:
            return prepareAdd();
        case ListCommand.COMMAND_WORD:
            return new ListCommand(arguments.trim());
        case DeleteCommand.COMMAND_WORD:
            return prepareDelete();
        case ClearCommand.COMMAND_WORD:
            return new ClearCommand(arguments.trim());
        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();
        case AddMeetingCommand.COMMAND_WORD:
            return prepareAddMeeting();
        default:
            String feedback = ERROR_INVALID_COMMAND + '\n' + MESSAGE_HELP;
            return new CommandResult(feedback);
        }
    }


    /**
     * Collate the user's input and verify the validity of the input value of each parameter.
     * Invalid inputs will be identified.
     * If input is valid, AddLessonCommand class is returned.
     *
     * @return AddLessonCommand if input is valid
     *         CommandResult if input is invalid, with the error description as a parameter
     */
    public Command prepareAdd() {
        try {
            String[] eventDescription = splitArgumentsAddLesson();
            checkNonNullValues(eventDescription);

            String day = eventDescription[DAY_INDEX].toLowerCase();
            int startTime = Integer.parseInt(eventDescription[STARTTIME_INDEX]);
            int endTime = Integer.parseInt(eventDescription[ENDTIME_INDEX]);
            String mode = eventDescription[MODE_INDEX].toLowerCase();

            checkDay(day);
            checkTime(startTime, endTime);
            checkMode(mode);

            String name = eventDescription[NAME_INDEX];
            String title = eventDescription[TITLE_INDEX];
            return new AddLessonCommand(name, title, day, startTime, endTime, mode);

        } catch (ArrayIndexOutOfBoundsException | NullPointerException npe) {
            return new CommandResult(ERROR_MISSING_PARAMETERS);
        } catch (MissingValueException mve) {
            return new CommandResult(ERROR_MISSING_VALUES);
        } catch (InvalidTimeException | NumberFormatException ite) {
            return new CommandResult(ERROR_INVALID_TIME);
        } catch (InvalidDayException ide) {
            return new CommandResult(ERROR_INVALID_DAY);
        } catch (InvalidModeException ime) {
            return new CommandResult(ERROR_INVALID_MODE);
        } catch (AssertionError ae) {
            logger.log(Level.INFO, "Assertion Error");
            return new CommandResult(ae.getMessage());
        }
    }

    /**
     * Checks the validity of the user's given startTime and endTime.
     *
     * @param startTime Time at which the event begins
     * @param endTime Time at which the event ends
     * @throws InvalidTimeException If the given hours and minutes are invalid, or if startTime is later than endTime
     */
    private void checkTime(int startTime, int endTime) throws InvalidTimeException {
        int startMinutes = startTime % 100;
        int endMinutes = endTime % 100;
        boolean invalidMinutes = startMinutes >= 60 || endMinutes >= 60;
        int startHours = startTime / 100;
        int endHours = endTime / 100;
        boolean invalidHours = startHours >= 24 || endHours >= 24;
        if (invalidMinutes || invalidHours || startTime > endTime) {
            throw new InvalidTimeException();
        }
    }

    /**
     * Checks that all parameters have a non-null value.
     *
     * @param eventDescription Array of user's input
     * @throws MissingValueException If at least one parameter has no value
     */
    private void checkNonNullValues(String[] eventDescription) throws MissingValueException {
        for (int i = 0; i < MODE_INDEX; i++) {
            if (eventDescription[i].length() == 0) {
                throw new MissingValueException();
            }
            assert (eventDescription[i].length() != 0) : "The parameters have non-null values";
        }
    }

    /**
     * Checks that the mode given by the user is either online or physical.
     *
     * @param mode String given by user
     * @throws InvalidModeException If mode is neither online nor physical
     */
    private void checkMode(String mode) throws InvalidModeException {
        if (mode.equals("online") || mode.equals("physical")) {
            return;
        }
        throw new InvalidModeException();
    }

    /**
     * Ensures that the 'day' parameter in user's input is a valid day.
     *
     * @param day String given by user
     * @throws InvalidDayException If value of 'day' does not correspond to an actual day
     */
    private void checkDay(String day) throws InvalidDayException {
        switch (day) {
        case "monday":
        case "tuesday":
        case "wednesday":
        case "thursday":
        case "friday":
        case "saturday":
        case "sunday":
            break;
        default:
            throw new InvalidDayException();
        }
    }

    /**
     * Try to parse the delete command to see if index has been done.
     *
     */
    public Command prepareDelete() {
        try {
            String name = getParametersByIndexFromInput(0,arguments);
            int index = Integer.parseInt(getParametersByIndexFromInput(1,arguments));
            return new DeleteCommand(name,index);
        } catch (NumberFormatException nfe) {
            logger.log(Level.INFO, "Invalid index to delete Error detected.");
            return new CommandResult(ERROR_INVALID_INDEX_FORMAT);
        }
    }

    private String getCommandFromInput(String input) {
        return input.split(" ")[0].trim().toLowerCase();
    }

    private String getParametersByIndexFromInput(int index,String input) {
        return input.split(" ")[index].trim().toLowerCase();
    }

    private String getArgumentsFromInput(String input) {
        String str = "";
        int spaceIndex = input.trim().indexOf(" ");
        if (spaceIndex != -1) {
            str = input.substring(spaceIndex + 1).trim();
        }
        return str;
    }

    private String[] splitArgumentsAddLesson() {
        String[] eventDescription = new String[6];
        String[] splitArguments = arguments.split(" ");
        int index = -1;
        for (String str : splitArguments) {
            if (containHeadings(str,HEADINGS_ADD_LESSON) == -1) {
                eventDescription[index] += " " + str;
                eventDescription[index] = eventDescription[index].trim();
            } else {
                index = containHeadings(str,HEADINGS_ADD_LESSON);
                eventDescription[index] = str.substring(str.indexOf("/") + 1);
            }
        }
        return eventDescription;
    }
    private String[] splitArgumentsAddMeeting() {
        String[] eventDescription = new String[5];
        String[] splitArguments = arguments.split(" ");
        int index = -1;
        for (String str : splitArguments) {
            if (containHeadings(str,HEADINGS_ADD_MEETING) == -1) {
                eventDescription[index] += " " + str;
                eventDescription[index] = eventDescription[index].trim();
            } else {
                index = containHeadings(str,HEADINGS_ADD_MEETING);
                eventDescription[index] = str.substring(str.indexOf("/") + 1);
            }
        }
        return eventDescription;
    }

    private int containHeadings(String str,String[] HEADINGS) {
        for (int i = 0; i < HEADINGS.length; i++) {
            if (str.contains(HEADINGS[i])) {
                int headingLength = HEADINGS[i].length();
                String subStr = str.substring(0, headingLength);
                if (subStr.equals(HEADINGS[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    public Command prepareAddMeeting() {
        try {
            String[] eventDescription = splitArgumentsAddMeeting();
            checkNonNullValues(eventDescription);

            //there is no name for meeting because meeting applies to everyone
            String day = eventDescription[DAY_INDEX - 1].toLowerCase();
            int startTime = Integer.parseInt(eventDescription[STARTTIME_INDEX - 1]);
            int endTime = Integer.parseInt(eventDescription[ENDTIME_INDEX - 1]);
            String mode = eventDescription[MODE_INDEX - 1].toLowerCase();

            checkDay(day);
            checkTime(startTime, endTime);
            checkMode(mode);

            String title = eventDescription[TITLE_INDEX];
            return new AddMeetingCommand(title, day, startTime, endTime, mode);

        } catch (ArrayIndexOutOfBoundsException | NullPointerException npe) {
            return new CommandResult(ERROR_MISSING_PARAMETERS);
        } catch (MissingValueException mve) {
            return new CommandResult(ERROR_MISSING_VALUES);
        } catch (InvalidTimeException | NumberFormatException ite) {
            return new CommandResult(ERROR_INVALID_TIME);
        } catch (InvalidDayException ide) {
            return new CommandResult(ERROR_INVALID_DAY);
        } catch (InvalidModeException ime) {
            return new CommandResult(ERROR_INVALID_MODE);
        } catch (AssertionError ae) {
            logger.log(Level.INFO, "Assertion Error");
            return new CommandResult(ae.getMessage());
        }
    }

}
