package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK_DEADLINE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK_PRIORITY;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskDeadline;
import seedu.address.model.task.TaskDescription;
import seedu.address.model.task.TaskName;
import seedu.address.model.task.TaskPriority;

/**
 * Edits the details of an existing task in the task list.
 */
public class EditTaskCommand extends Command {

    public static final String COMMAND_WORD = "edittask";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the task identified "
            + "by the index number used in the displayed task list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "TASK_NAME] "
            + "[" + PREFIX_TASK_DESCRIPTION + "TASK_DESCRIPTION] "
            + "[" + PREFIX_TASK_PRIORITY + "TASK_PRIORITY] "
            + "[" + PREFIX_TASK_DEADLINE + "TASK_DEADLINE] "
            + "\nExample: " + COMMAND_WORD + " 1 "
            + PREFIX_NAME + "name "
            + PREFIX_TASK_DESCRIPTION + "new description "
            + PREFIX_TASK_PRIORITY + "HIGH "
            + PREFIX_TASK_DEADLINE + "30-03-2024 10:00";

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Edited Task: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_TASK = "Duplicate task exists in the task list.";

    private final Index index;
    private final EditTaskDescriptor editTaskDescriptor;

    /**
     * @param index of the task in the task list to edit.
     * @param editTaskDescriptor details to edit the task with.
     */
    public EditTaskCommand(Index index, EditTaskDescriptor editTaskDescriptor) {
        requireNonNull(index);
        requireNonNull(editTaskDescriptor);

        this.index = index;
        this.editTaskDescriptor = new EditTaskDescriptor(editTaskDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Task taskToEdit = lastShownList.get(index.getZeroBased());
        Task editedTask = createEditedTask(taskToEdit, editTaskDescriptor);

        if (!taskToEdit.equals(editedTask) && model.hasTask(editedTask)) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        }

        // Updates the task list.
        model.setTask(taskToEdit, editedTask);

        return new CommandResult(String.format(MESSAGE_EDIT_TASK_SUCCESS, Messages.formatTask(taskToEdit)));
    }

    /**
     * Creates and returns a {@code Task} with the details of {@code taskToEdit}
     * edited with {@code editTaskDescriptor}.
     */
    private static Task createEditedTask(Task taskToEdit, EditTaskDescriptor editTaskDescriptor) {
        assert taskToEdit != null;

        TaskName updatedName = editTaskDescriptor.getName().orElse(taskToEdit.getName());
        TaskDescription updatedDescription = editTaskDescriptor.getDescription().orElse(taskToEdit.getDescription());
        TaskPriority updatedPriority = editTaskDescriptor.getPriority().orElse(taskToEdit.getPriority());
        TaskDeadline updatedDeadline = editTaskDescriptor.getDeadline().orElse(taskToEdit.getDeadline());

        return new Task(updatedName, updatedDescription, updatedPriority, taskToEdit.getStatus(), updatedDeadline);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditTaskCommand)) {
            return false;
        }

        EditTaskCommand otherEditCommand = (EditTaskCommand) other;
        return index.equals(otherEditCommand.index)
                && editTaskDescriptor.equals(otherEditCommand.editTaskDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editTaskDescriptor", editTaskDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the task with. Each non-empty field value will
     * replace the corresponding field value of the task.
     */
    public static class EditTaskDescriptor {
        private TaskName taskName;
        private TaskDescription taskDescription;
        private TaskPriority taskPriority;
        private TaskDeadline taskDeadline;

        public EditTaskDescriptor() {
        }

        /**
         * Copy constructor.
         */
        public EditTaskDescriptor(EditTaskDescriptor toCopy) {
            setName(toCopy.taskName);
            setDescription(toCopy.taskDescription);
            setPriority(toCopy.taskPriority);
            setDeadline(toCopy.taskDeadline);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(taskName, taskDescription, taskPriority, taskDeadline);
        }

        public void setName(TaskName taskName) {
            this.taskName = taskName;
        }

        public Optional<TaskName> getName() {
            return Optional.ofNullable(taskName);
        }

        public void setDescription(TaskDescription taskDescription) {
            this.taskDescription = taskDescription;
        }

        public Optional<TaskDescription> getDescription() {
            return Optional.ofNullable(taskDescription);
        }

        public void setPriority(TaskPriority taskPriority) {
            this.taskPriority = taskPriority;
        }

        public Optional<TaskPriority> getPriority() {
            return Optional.ofNullable(taskPriority);
        }

        public void setDeadline(TaskDeadline taskDeadline) {
            this.taskDeadline = taskDeadline;
        }

        public Optional<TaskDeadline> getDeadline() {
            return Optional.ofNullable(taskDeadline);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditTaskDescriptor)) {
                return false;
            }

            EditTaskDescriptor otherEditTaskDescriptor = (EditTaskDescriptor) other;
            return Objects.equals(taskName, otherEditTaskDescriptor.taskName)
                    && Objects.equals(taskDescription, otherEditTaskDescriptor.taskDescription)
                    && Objects.equals(taskPriority, otherEditTaskDescriptor.taskPriority)
                    && Objects.equals(taskDeadline, otherEditTaskDescriptor.taskDeadline);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", taskName)
                    .add("description", taskDescription)
                    .add("priority", taskPriority)
                    .add("deadline", taskDeadline)
                    .toString();
        }
    }
}
