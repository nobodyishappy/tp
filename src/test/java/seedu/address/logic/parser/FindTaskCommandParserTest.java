package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindTaskCommand;
import seedu.address.model.task.TaskNameContainsKeywordsPredicate;

class FindTaskCommandParserTest {
    private FindTaskCommandParser parser = new FindTaskCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindTaskCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindTaskCommand expectedFindTaskCommand =
                new FindTaskCommand(new TaskNameContainsKeywordsPredicate(Arrays.asList("2101", "2103")));
        assertParseSuccess(parser, "2101 2103", expectedFindTaskCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n 2101 \n \t 2103  \t", expectedFindTaskCommand);
    }
}
