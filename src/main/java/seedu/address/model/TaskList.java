package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
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
     * {@code editedTask}.
     * {@code target} must exist in the task list.
     * The task of {@code editedTask} must not be the same as another
     * existing task in the task list.
     */
    public void setTask(Task target, Task editedTask) {
        requireAllNonNull(target, editedTask);

        int index = observableList.indexOf(target);

        /* todo
        if (index == -1) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        if (!target.equals(editedTask) && contains(editedPerson)) {
            throw new DuplicateTaskException();
        } */

        observableList.set(index, editedTask);
    }

    public boolean hasTask(Task task) {
        return observableList.contains(task);
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
