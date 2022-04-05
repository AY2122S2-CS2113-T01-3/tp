package seedu.meetingjio.commands;

import org.junit.jupiter.api.Test;
import seedu.meetingjio.parser.Parser;
import seedu.meetingjio.timetables.MasterTimetable;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.meetingjio.common.ErrorMessages.ERROR_NO_USER_TO_ADD_MEETING;
import static seedu.meetingjio.common.ErrorMessages.ERROR_DUPLICATE_MEETING;
import static seedu.meetingjio.common.ErrorMessages.ERROR_OVERLAPPING_MEETING;

public class AddMeetingTest {
    public static Logger logger = Logger.getLogger(Parser.class.getName());
    MasterTimetable masterTimetable = new MasterTimetable();

    @Test
    public void addMeetingNoUsers() {
        ClearCommand clearCommand = new ClearCommand("all");
        clearCommand.execute(masterTimetable);
        AddMeetingCommand addMeetingCommand = new AddMeetingCommand("meeting", "Thursday",
                1230, 1330, "online"
        );
        assertEquals(ERROR_NO_USER_TO_ADD_MEETING, addMeetingCommand.execute(masterTimetable));
    }


    @Test
    public void addDuplicateMeetings() {
        ClearCommand clearCommand = new ClearCommand("all");
        clearCommand.execute(masterTimetable);
        AddUserCommand addUserOne = new AddUserCommand("john");
        addUserOne.execute(masterTimetable);
        AddMeetingCommand addMeetingCommandOne = new AddMeetingCommand("meeting", "Thursday",
                1230, 1330, "online"
        );
        AddMeetingCommand addMeetingCommandTwo = new AddMeetingCommand("meeting", "Thursday",
                1230, 1330, "online"
        );
        addMeetingCommandOne.execute(masterTimetable);
        assertEquals(ERROR_DUPLICATE_MEETING, addMeetingCommandTwo.execute(masterTimetable));
    }

    @Test
    public void addOverlappingMeetings() {
        ClearCommand clearCommand = new ClearCommand("all");
        clearCommand.execute(masterTimetable);
        AddUserCommand addUserOne = new AddUserCommand("john");
        addUserOne.execute(masterTimetable);
        AddLessonCommand addCommand = new AddLessonCommand(
                "John", "CS2113", "Monday",
                1200, 1300, "online"
        );
        addCommand.execute(masterTimetable);
        AddMeetingCommand addMeetingCommandOne = new AddMeetingCommand("meeting", "Monday",
                1230, 1330, "online"
        );
        AddMeetingCommand addMeetingCommandTwo = new AddMeetingCommand("meeting", "Monday",
                1230, 1330, "online"
        );
        addMeetingCommandOne.execute(masterTimetable);
        assertEquals(ERROR_OVERLAPPING_MEETING, addMeetingCommandTwo.execute(masterTimetable));
    }
}
