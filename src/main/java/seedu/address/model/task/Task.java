package seedu.address.model.task;

import java.util.Objects;

/**
 * The representation of a task added by the user.
 */
public class Task {
    private TaskName name;
    private TaskDescription description;
    private TaskPriority priority;
    private TaskStatus status;
    private TaskDeadline deadline;

    /**
     * The constructor of the class.
     * @param name Name of the task.
     * @param description Description of the task.
     * @param status Status of the task.
     */
    public Task(TaskName name, TaskDescription description, TaskPriority priority, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.deadline = new TaskDeadline();
    }

    /**
     * The constructor of the class.
     * @param name Name of the task.
     * @param description Description of the task.
     * @param status Status of the task.
     * @param deadline Deadline of the task.
     */
    public Task(TaskName name,
                TaskDescription description,
                TaskPriority priority,
                TaskStatus status,
                TaskDeadline deadline) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.deadline = deadline;
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
     * @return The status of the task.
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * Gets the priority of a task.
     * @return The priority of the task.
     */
    public TaskPriority getPriority() {
        return priority;
    }

    /**
     * Gets the deadline of a task.
     * @return The deadline of the task.
     */
    public TaskDeadline getDeadline() {
        return deadline;
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
                && this.description.equals(otherTask.description)
                && this.priority.equals(otherTask.priority)
                && this.deadline.equals(otherTask.deadline);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, description, priority, deadline);
    }

    /**
     * Compare with other tasks for sorting in tags
     * @param otherTask Task to be compared to
     * @return Value of the comparison
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
