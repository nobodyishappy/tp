package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnmarkTaskCommand;
public class UnmarkTaskCommandParserTest {
    private UnmarkTaskCommandParser parser = new UnmarkTaskCommandParser();

    @Test
    public void parse_validArgs_returnsUnmarkTaskCommand() {
        assertParseSuccess(parser, "1 2", new UnmarkTaskCommand(new Index[] { INDEX_FIRST, INDEX_SECOND }));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkTaskCommand.MESSAGE_USAGE));
    }
}
