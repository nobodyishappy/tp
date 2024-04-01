package seedu.address.model.task;


import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import seedu.address.model.util.Priority;

/**
 * Represents a Task's priority in the task list.
 */
public class TaskPriority {

    public static final String MESSAGE_CONSTRAINTS =
            "Task priority can take either an integer or low, medium, high, and it should not be blank";
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
        requireNonNull(priority);
        checkArgument(isValidTaskPriority(priority), MESSAGE_CONSTRAINTS);

        priority = priority.toLowerCase();

        if (priority.equals("low") || priority.equals("1")) {
            this.taskPriority = Priority.LOW;
        } else if (priority.equals("medium") || priority.equals("2")) {
            this.taskPriority = Priority.MEDIUM;
        } else if (priority.equals("high") || priority.equals("3")) {
            this.taskPriority = Priority.HIGH;
        }
    }

    /**
     * Returns an int of {@code TaskPriority}.
     */
    public int getValue() {
        return taskPriority.getValue();
    }

    /**
     * Returns true if a given string is a valid description.
     */
    public static boolean isValidTaskPriority(String test) {
        test = test.toLowerCase();

        if (test.equals("low") || test.equals("medium") || test.equals("high")) {
            return true;
        }

        if (test.equals("1") || test.equals("2") || test.equals("3")) {
            return true;
        }

        return false;
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

    @Override
    public int hashCode() {
        return taskPriority.hashCode();
    }
}
