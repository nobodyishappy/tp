package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes the people identified using their displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the people identified by the index numbers used in the displayed person list.\n"
            + "Parameters: INDEX [MORE_INDICES] (must be distinct positive integers)\n"
            + "Example: " + COMMAND_WORD + " 1 2";

    public static final String MESSAGE_DELETE_PEOPLE_SUCCESS = "Deleted people: %1$s";

    private final Index[] targetIndices;

    public DeleteCommand(Index[] targetIndices) {
        this.targetIndices = targetIndices;
    }

    private void verifyAllWithinRange(Index[] targetIndices, int range) throws CommandException {
        if (Arrays.stream(targetIndices).anyMatch(targetIndex -> targetIndex.getZeroBased() >= range)) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();
        verifyAllWithinRange(targetIndices, lastShownList.size());

        Person[] peopleToDelete = Arrays.stream(targetIndices).distinct()
                .map(targetIndex -> lastShownList.get(targetIndex.getZeroBased()))
                .toArray(Person[]::new);

        Arrays.stream(peopleToDelete).forEach(model::deletePerson);

        return new CommandResult(String.format(MESSAGE_DELETE_PEOPLE_SUCCESS, Messages.format(peopleToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return Arrays.equals(targetIndices, otherDeleteCommand.targetIndices);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndices", Arrays.toString(targetIndices))
                .toString();
    }
}
