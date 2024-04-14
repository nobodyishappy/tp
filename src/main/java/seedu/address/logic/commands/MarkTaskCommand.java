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
 * Mark the tasks identified using their displayed index from the task list as done.
 */
public class MarkTaskCommand extends Command {

    public static final String COMMAND_WORD = "marktask";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the tasks identified by the index numbers used in the displayed task list as done. \n"
            + "Parameter: INDEX [MORE_INDICES] (must be positive integers) \n"
            + "Example: " + COMMAND_WORD + " 1 2";

    public static final String MESSAGE_SUCCESS = "Tasks have been marked as done: %1$s";

    private final Index[] targetIndices;

    public MarkTaskCommand(Index[] targetIndices) {
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

        Task[] tasksToMark = Arrays.stream(targetIndices).distinct()
                .map(targetIndex -> lastShownList.get(targetIndex.getZeroBased()))
                .toArray(Task[]::new);

        Arrays.stream(tasksToMark).forEach(taskToMark -> {
            Task editedTask = new Task(taskToMark);
            editedTask.getStatus().setAsDone();
            model.setTask(taskToMark, editedTask);
        });

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(tasksToMark)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MarkTaskCommand)) {
            return false;
        }

        MarkTaskCommand otherMarkTaskCommand = (MarkTaskCommand) other;
        return Arrays.equals(targetIndices, otherMarkTaskCommand.targetIndices);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndices", Arrays.toString(targetIndices))
                .toString();
    }
}
