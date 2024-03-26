package seedu.address.testutil;

import seedu.address.model.task.*;

/**
 * A utility class to help with building Task objects.
 */
public class TaskBuilder {

    public static final String DEFAULT_TASK_NAME = "Implement Test";
    public static final String DEFAULT_TASK_DESCRIPTION = "Test to test the code";

    private TaskName taskName;
    private TaskDescription taskDescription;
    private TaskPriority taskPriority;

    /**
     * Creates a {@code TaskBuilder} with the default details.
     */
    public TaskBuilder() {
        taskName = new TaskName(DEFAULT_TASK_NAME);
        taskDescription = new TaskDescription(DEFAULT_TASK_DESCRIPTION);
    }

    /**
     * Initializes the TaskBuilder with the data of {@code taskToCopy}.
     */
    public TaskBuilder(Task taskToCopy) {
        taskName = taskToCopy.getName();
        taskDescription = taskToCopy.getDescription();
    }

    /**
     * Sets the {@code TaskName} of the {@code Task} that we are building.
     */
    public TaskBuilder withTaskName(String taskName) {
        this.taskName = new TaskName(taskName);
        return this;
    }

    /**
     * Sets the {@code TaskDescription} of the {@code Task} that we are building.
     */
    public TaskBuilder withTaskDescription(String taskDescription) {
        this.taskDescription = new TaskDescription(taskDescription);
        return this;
    }

    /**
     * Sets the {@code TaskPriority} of the {@code Task} that we are building.
     */
    public TaskBuilder withTaskPriority(String taskPriority) {
        this.taskPriority = new TaskPriority(taskPriority);
        return this;
    }

    public Task build() {
        return new Task(taskName, taskDescription, new TaskPriority(), new TaskStatus());
    }
}
