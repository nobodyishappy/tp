package seedu.address.model.task;

import java.util.Objects;

/**
 * The representation of a task added by the user.
 */
public class Task {
    private TaskName name;
    private TaskDescription description;
    private TaskStatus status;

    /**
     * The constructor of the class.
     * @param description Description of the task.
     */
    public Task(TaskName name, TaskDescription description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    /**
     * Gets the name of a task.
     * @return The name of the task.
     */
    public TaskName getName() {
        return name;
    }

    /**
     * Gets the description of a task.
     * @return The description of the task.
     */
    public TaskDescription getDescription() {
        return description;
    }

    /**
     * Gets the status of a task.
     * @return The statis of the task.
     */
    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        //instanceof handles null
        if (!(other instanceof Task)) {
            return false;
        }

        Task otherTask = (Task) other;
        return this.name.equals(otherTask.name)
                && this.description.equals(otherTask.description);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, description);
    }

    /**
     * Compare with other tasks for sorting in tags
     * @param otherTask Task to be compared to
     * @return value of the comparison
     */
    public int compare(Task otherTask) {
        if (this.getName().taskName.compareTo(otherTask.getName().taskName) == 0) {
            return this.getDescription()
                    .taskDescription.compareTo(otherTask.getDescription().taskDescription);
        } else {
            return this.getName()
                    .taskName.compareTo(otherTask.getName().taskName);
        }
    }
}