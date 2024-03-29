package seedu.address.model.task;

import seedu.address.model.util.Priority;

/**
 * Represents a Task's priority in the task list.
 */
public class TaskPriority {

    private Priority taskPriority;

    /**
     * Constructs an {@code TaskPriority}.
     */
    public TaskPriority() {
        taskPriority = Priority.LOW;
    }

    /**
     * Constructs an {@code TaskPriority}.
     */
    public TaskPriority(String priority) {
        if (priority.equals("LOW") || priority.equals("1")) {
            this.taskPriority = Priority.LOW;
        } else if (priority.equals("MEDIUM") || priority.equals("2")) {
            this.taskPriority = Priority.MEDIUM;
        } else {
            this.taskPriority = Priority.HIGH;
        }
    }

    /**
     * Returns an int of {@code TaskPriority}.
     */
    public int getValue() {
        return taskPriority.getValue();
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
