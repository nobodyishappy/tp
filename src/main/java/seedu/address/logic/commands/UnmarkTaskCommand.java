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
 * Mark the tasks identified using their displayed index from the task list as undone.
 */
public class UnmarkTaskCommand extends Command {

    public static final String COMMAND_WORD = "unmarktask";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the tasks identified by the index numbers used in the displayed task list as undone. \n"
            + "Parameter: INDEX [MORE_INDICES] (must be distinct positive integers) \n"
            + "Example: " + COMMAND_WORD + " 1 2";
    public static final String MESSAGE_UNMARK_TASK_SUCCESS = "Tasks have been marked as undone: %1$s";

    private final Index[] targetIndices;

    public UnmarkTaskCommand(Index[] targetIndices) {
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

        Task[] tasksToUnmark = Arrays.stream(targetIndices).distinct()
                .map(targetIndex -> lastShownList.get(targetIndex.getZeroBased()))
                .toArray(Task[]::new);

        Arrays.stream(tasksToUnmark).forEach(taskToUnmark -> {
            Task editedTask = new Task(taskToUnmark);
            editedTask.getStatus().setAsUndone();
            model.setTask(taskToUnmark, editedTask);
        });

        return new CommandResult(String.format(MESSAGE_UNMARK_TASK_SUCCESS, Messages.format(tasksToUnmark)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnmarkTaskCommand)) {
            return false;
        }

        UnmarkTaskCommand otherUnmarkTaskCommand = (UnmarkTaskCommand) other;
        return Arrays.equals(targetIndices, otherUnmarkTaskCommand.targetIndices);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndices", Arrays.toString(targetIndices))
                .toString();
    }
}
