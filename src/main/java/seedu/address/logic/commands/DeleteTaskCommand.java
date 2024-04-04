package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.task.Task;

/**
 * Deletes the tasks identified using their displayed index from the task list.
 */
public class DeleteTaskCommand extends Command {

    public static final String COMMAND_WORD = "deletetask";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the tasks identified by the index numbers used in the displayed task list.\n"
            + "Parameters: INDEX [MORE_INDICES] (must be distinct positive integers)\n"
            + "Example: " + COMMAND_WORD + " 1 2";

    public static final String MESSAGE_DELETE_TASKS_SUCCESS = "Deleted tasks: %1$s";

    private final Index[] targetIndices;

    /**
     * Creates an DeleteTaskCommand to delete the specified {@code targetIndices}
     */
    public DeleteTaskCommand(Index[] targetIndices) {
        this.targetIndices = targetIndices;
    }

    private void verifyAllWithinRange(Index[] targetIndices, int range) throws CommandException {
        if (Arrays.stream(targetIndices).anyMatch(targetIndex -> targetIndex.getZeroBased() >= range)) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Task> lastShownList = model.getFilteredTaskList();
        verifyAllWithinRange(targetIndices, lastShownList.size());

        Task[] tasksToDelete = Arrays.stream(targetIndices).distinct()
                .map(targetIndex -> lastShownList.get(targetIndex.getZeroBased()))
                .toArray(Task[]::new);

        Arrays.stream(tasksToDelete).forEach(model::deleteTask);

        return new CommandResult(String.format(MESSAGE_DELETE_TASKS_SUCCESS, Messages.format(tasksToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteTaskCommand)) {
            return false;
        }

        DeleteTaskCommand otherDeleteCommand = (DeleteTaskCommand) other;
        return Arrays.equals(targetIndices, otherDeleteCommand.targetIndices);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndices", Arrays.toString(targetIndices))
                .toString();
    }
}
