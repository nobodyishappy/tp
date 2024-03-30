package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.FindTaskCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.task.TaskNameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindTaskCommand object
 */
public class FindTaskCommandParser implements Parser<FindTaskCommand> {

    /**
     * Parses {@code userInput} in the context of FindTaskCommand and returns
     * a FindTaskCommand object for execution.
     * @param userInput The input to parse into a command object.
     * @throws ParseException if {@code userInput} does not conform the expected format
     */
    @Override
    public FindTaskCommand parse(String userInput) throws ParseException {
        String trimmedInput = userInput.trim();
        if (trimmedInput.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindTaskCommand.MESSAGE_USAGE));
        }

        String[] taskNameKeywords = trimmedInput.split("\\s+");

        return new FindTaskCommand(new TaskNameContainsKeywordsPredicate(Arrays.asList(taskNameKeywords)));
    }
}
