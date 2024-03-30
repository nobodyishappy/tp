package seedu.address.testutil;

import seedu.address.model.TaskList;
import seedu.address.model.task.Task;

/**
 * A utility class containing a list of {@code Task} objects to be used in tests.
 */
public class TypicalTasks {
    public static final Task TASK_1 = new TaskBuilder().withTaskName("task1").build();
    public static final Task TASK_2 = new TaskBuilder().withTaskName("task2").build();
    public static final Task TASK_3 = new TaskBuilder().withTaskName("task3").build();

    private TypicalTasks() {}

    /**
     * Returns an {@code TaskList} with all the sample tasks.
     */
    public static TaskList getTypicalTaskList() {
        TaskList taskList = new TaskList();
        taskList.addTask(TASK_1);
        taskList.addTask(TASK_2);
        taskList.addTask(TASK_3);
        return taskList;
    }
}
