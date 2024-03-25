package seedu.address.model.task;

import seedu.address.model.util.Priority;

/**
 * Represents a Task's priority in the task list.
 */
public class TaskPriority {

    private final Priority taskPriority;

    /**
     * Constructs an {@code TaskStatus}.
     */
    public TaskPriority() {
        taskPriority = Priority.LOW;
    }

    @Override
    public String toString() {
        if (taskPriority.equals(Priority.LOW)) {
            return "LOW";
        } else if (taskPriority.equals(Priority.MEDIUM)) {
            return "MEDIUM";
        } else {
            return "HIGH";
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TaskPriority)) {
            return false;
        }

        TaskPriority otherPriority = (TaskPriority) other;
        return taskPriority == otherPriority.taskPriority;
    }
}
