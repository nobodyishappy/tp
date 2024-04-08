package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnmarkTaskCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UnmarkTaskCommand object
 */
public class UnmarkTaskCommandParser implements Parser<UnmarkTaskCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UnmarkTaskCommand
     * and returns a UnmarkTaskCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public UnmarkTaskCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkTaskCommand.MESSAGE_USAGE));
        }

        String[] stringIndices = trimmedArgs.split("\\s+");

        try {
            Index[] indices = new Index[stringIndices.length];
            for (int i = 0; i < stringIndices.length; i++) {
                indices[i] = ParserUtil.parseIndex(stringIndices[i]);
            }
            return new UnmarkTaskCommand(indices);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkTaskCommand.MESSAGE_USAGE), pe);
        }
    }
}
