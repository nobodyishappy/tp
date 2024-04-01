package seedu.address.testutil;

import seedu.address.model.task.Task;
import seedu.address.model.task.TaskDeadline;
import seedu.address.model.task.TaskDescription;
import seedu.address.model.task.TaskName;
import seedu.address.model.task.TaskPriority;
import seedu.address.model.task.TaskStatus;

/**
 * A utility class to help with building Task objects.
 */
public class TaskBuilder {

    public static final String DEFAULT_TASK_NAME = "Implement Test";
    public static final String DEFAULT_TASK_DESCRIPTION = "Test to test the code";
    public static final String DEFAULT_TASK_DEADLINE = "Empty";
    public static final String DEFAULT_TASK_PRIORITY = "low";

    private TaskName taskName;
    private TaskDescription taskDescription;
    private TaskPriority taskPriority;
    private TaskDeadline taskDeadline;

    /**
     * Creates a {@code TaskBuilder} with the default details.
     */
    public TaskBuilder() {
        taskName = new TaskName(DEFAULT_TASK_NAME);
        taskDescription = new TaskDescription(DEFAULT_TASK_DESCRIPTION);
        taskPriority = new TaskPriority(DEFAULT_TASK_PRIORITY);
        taskDeadline = new TaskDeadline();
    }

    /**
     * Initializes the TaskBuilder with the data of {@code taskToCopy}.
     */
    public TaskBuilder(Task taskToCopy) {
        taskName = taskToCopy.getName();
        taskDescription = taskToCopy.getDescription();
        taskPriority = taskToCopy.getPriority();
        taskDeadline = taskToCopy.getDeadline();
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

    /**
     * Sets the {@code TaskDeadline} of the {@code Task} that we are building.
     */
    public TaskBuilder withTaskDeadline(String taskDeadline) {
        this.taskDeadline = new TaskDeadline(taskDeadline);
        return this;
    }

    public Task build() {
        return new Task(taskName, taskDescription, taskPriority, new TaskStatus(), taskDeadline);
    }
}
