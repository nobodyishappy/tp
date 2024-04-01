package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.task.Task;

/**
 * A class that stores the tasks from users.
 */
public class TaskList {
    //private ArrayList<Task> taskList;
    private ObservableList<Task> observableList;

    /**
     * Constructor of the class.
     */
    public TaskList() {
        observableList = FXCollections.observableArrayList();
    }

    /**
     * Creates a TaskList using the Tasks in the {@code toBeCopied}
     */
    public TaskList(TaskList toBeCopied) {
        this();

        requireNonNull(toBeCopied);
        setTaskList(toBeCopied);
    }

    public void setTaskList(TaskList tasks) {
        observableList.setAll(tasks.observableList);
    }

    /**
     * Adds a task to the end of list.
     *
     * @param task The task to be added to the list.
     */
    public void addTask(Task task) {
        observableList.add(task);
    }

    /**
     * Deletes a task based on the index of list.
     *
     * @param task The task to be deleted.
     */
    public void deleteTask(Task task) {
        observableList.remove(task);
    }

    public ObservableList<Task> getSerializeTaskList() {
        return observableList;
    }

    /**
     * Replaces the given task {@code target} in the list with
     * {@code target} must exist in the task list. 
     * {@code editedTask}.
     * The task of {@code editedTask} must not be the same as another
     * existing task in the task list.
     */
    public void setTask(Task target, Task editedTask) {
        requireAllNonNull(target, editedTask);

        int index = observableList.indexOf(target);
        observableList.set(index, editedTask);
    }

    public boolean hasTask(Task task) {
        return observableList.contains(task);
    }

    /**
     * Sorts tasks based on the task priority.
     */
    public void sortByPriority() {
        observableList.sort(new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {

                //Sort by status
                if (o1.getStatus().compareTo(o2.getStatus()) != 0) {
                    return o1.getStatus().getTaskStatus() ? 1 : -1;
                }

                //Sort by Priority
                if (o2.getPriority().getValue() - o1.getPriority().getValue()  != 0) {
                    return o2.getPriority().getValue() - o1.getPriority().getValue();
                }

                //Sort by deadline
                if (o1.getDeadline().taskDeadline != null && o2.getDeadline().taskDeadline != null) {
                    return o1.getDeadline().taskDeadline.compareTo(o2.getDeadline().taskDeadline);
                }

                //Sort by tasks with deadline
                if (o1.getDeadline() != null) {
                    return -1;
                } else if (o2.getDeadline() != null) {
                    return 1;
                } 
                return 0;
            }
        });
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TaskList)) {
            return false;
        }

        TaskList otherTaskList = (TaskList) other;
        return observableList.equals(otherTaskList.observableList);
    }
}
