package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.task.Task;
import seedu.address.testutil.TaskBuilder;

public class TaskListTest {

    private TaskList taskList;
    private TaskList sortedList;
    private TaskList toSortList;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
        sortedList = new TaskList();
        toSortList = new TaskList();
        task1 = new TaskBuilder().withTaskName("Task 1").build();
        task2 = new TaskBuilder().withTaskName("Task 2").build();
        task3 = new TaskBuilder().withTaskName("Task 3").build();
    }

    @Test
    public void addTask_success() {
        taskList.addTask(task1);
        assertTrue(taskList.hasTask(task1));
    }

    @Test
    public void hasTask_success() {
        taskList.addTask(task1);
        assertTrue(taskList.hasTask(task1));
    }

    @Test
    public void hasTask_failure() {
        assertFalse(taskList.hasTask(task1));
    }

    @Test
    public void getSerializeTaskList_success() {
        taskList.addTask(task1);
        taskList.addTask(task2);
        assertEquals(2, taskList.getSerializeTaskList().size());
    }

    @Test
    public void isValidPrioritySort() {
        task1 = new TaskBuilder().withTaskName("Task 1").withTaskPriority("low").build();
        task2 = new TaskBuilder().withTaskName("Task 2").withTaskPriority("medium").build();
        task3 = new TaskBuilder().withTaskName("Task 3").withTaskPriority("high").build();

        sortedList.addTask(task3);
        sortedList.addTask(task2);
        sortedList.addTask(task1);

        toSortList.addTask(task2);
        toSortList.addTask(task1);
        toSortList.addTask(task3);

        assertNotEquals(toSortList, sortedList);

        toSortList.sortByPriority();

        assertEquals(toSortList, sortedList);
    }

    @Test
    public void isValidDeadlineSort() {
        task1 = new TaskBuilder().withTaskName("Task 1").withTaskDeadline("10-10-2024 10:00").build();
        task2 = new TaskBuilder().withTaskName("Task 2").withTaskDeadline("11-10-2024 10:00").build();
        task3 = new TaskBuilder().withTaskName("Task 3").withTaskDeadline("12-10-2024 10:00").build();

        sortedList.addTask(task1);
        sortedList.addTask(task2);
        sortedList.addTask(task3);

        toSortList.addTask(task2);
        toSortList.addTask(task1);
        toSortList.addTask(task3);

        assertNotEquals(toSortList, sortedList);

        toSortList.sortByPriority();

        assertEquals(toSortList, sortedList);
    }
}
