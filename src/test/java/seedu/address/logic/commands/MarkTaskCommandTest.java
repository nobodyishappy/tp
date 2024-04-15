package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;
import static seedu.address.testutil.TypicalTasks.getTypicalTaskList;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.TaskList;
import seedu.address.model.UserPrefs;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskStatus;
import seedu.address.testutil.TaskBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code MarkTaskCommand}.
 */
public class MarkTaskCommandTest {

    private Model model = new ModelManager(new AddressBook(), getTypicalTaskList(), new UserPrefs());

    @Test
    public void execute_singleValidIndex_success() {
        Task taskToMark = model.getTaskList().getSerializeTaskList().get(INDEX_FIRST.getZeroBased());
        MarkTaskCommand markTaskCommand = new MarkTaskCommand(new Index[] { INDEX_FIRST });

        String expectedMessage = String.format(MarkTaskCommand.MESSAGE_SUCCESS,
                Messages.format(taskToMark));

        ModelManager expectedModel = new ModelManager(
                model.getAddressBook(), new TaskList(model.getTaskList()), new UserPrefs());
        Task editedTask = new Task(taskToMark.getName(), taskToMark.getDescription(), taskToMark.getPriority(),
                new TaskStatus(), taskToMark.getDeadline());
        editedTask.getStatus().setAsDone();
        expectedModel.setTask(taskToMark, editedTask);

        assertCommandSuccess(markTaskCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleValidIndex_success() {
        Task[] tasksToMark = new Task[] {
                model.getTaskList().getSerializeTaskList().get(INDEX_FIRST.getZeroBased()),
                model.getTaskList().getSerializeTaskList().get(INDEX_SECOND.getZeroBased())
        };

        MarkTaskCommand markTaskCommand = new MarkTaskCommand(new Index[] { INDEX_FIRST, INDEX_SECOND });

        String expectedMessage = String.format(MarkTaskCommand.MESSAGE_SUCCESS,
                Messages.format(tasksToMark));

        ModelManager expectedModel = new ModelManager(
                model.getAddressBook(), new TaskList(model.getTaskList()), new UserPrefs());
        Arrays.stream(tasksToMark).forEach(taskToMark -> {
            Task editedTask = new Task(taskToMark.getName(),
                    taskToMark.getDescription(),
                    taskToMark.getPriority(),
                    new TaskStatus(),
                    taskToMark.getDeadline());
            editedTask.getStatus().setAsDone();
            expectedModel.setTask(taskToMark, editedTask);
        });

        assertCommandSuccess(markTaskCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateValidIndex_success() {
        Task taskToMark = model.getTaskList().getSerializeTaskList().get(INDEX_FIRST.getZeroBased());
        MarkTaskCommand markTaskCommand = new MarkTaskCommand(new Index[] { INDEX_FIRST, INDEX_FIRST });

        String expectedMessage = String.format(MarkTaskCommand.MESSAGE_SUCCESS,
                Messages.format(taskToMark));

        ModelManager expectedModel = new ModelManager(
                model.getAddressBook(), new TaskList(model.getTaskList()), new UserPrefs());
        Task editedTask = new Task(taskToMark.getName(), taskToMark.getDescription(), taskToMark.getPriority(),
                new TaskStatus(), taskToMark.getDeadline());
        editedTask.getStatus().setAsDone();
        expectedModel.setTask(expectedModel.getTaskList().getSerializeTaskList().get(INDEX_FIRST.getZeroBased()),
                editedTask);

        assertCommandSuccess(markTaskCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_singleInvalidTaskIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getTaskList().getSerializeTaskList().size() + 1);
        MarkTaskCommand markTaskCommand = new MarkTaskCommand(new Index[] { outOfBoundIndex });

        assertCommandFailure(markTaskCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_duplicateInvalidTaskIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getTaskList().getSerializeTaskList().size() + 1);
        MarkTaskCommand markTaskCommand = new MarkTaskCommand(new Index[] { outOfBoundIndex, outOfBoundIndex });

        assertCommandFailure(markTaskCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_someInvalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getTaskList().getSerializeTaskList().size() + 1);
        MarkTaskCommand markTaskCommand = new MarkTaskCommand(new Index[] { outOfBoundIndex, INDEX_FIRST });

        assertCommandFailure(markTaskCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_allInvalidIndex_throwsCommandException() {
        MarkTaskCommand markTaskCommand = new MarkTaskCommand(new Index[] {
                Index.fromOneBased(model.getTaskList().getSerializeTaskList().size() + 1),
                Index.fromOneBased(model.getTaskList().getSerializeTaskList().size() + 2)
        });

        assertCommandFailure(markTaskCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_taskWithoutDeadline() {
        Task taskWithoutDeadline = new TaskBuilder().withTaskName("Task 1").withTaskDeadline("Empty").build();
        model.addTask(taskWithoutDeadline);
        Index noDeadlineTask = Index.fromOneBased(model.getTaskList().getSerializeTaskList().size());
        MarkTaskCommand markTaskCommand = new MarkTaskCommand(new Index[] { noDeadlineTask });

        String expectedMessage = String.format(MarkTaskCommand.MESSAGE_SUCCESS,
                Messages.format(taskWithoutDeadline));

        ModelManager expectedModel = new ModelManager(new AddressBook(), model.getTaskList(), new UserPrefs());

        assertCommandSuccess(markTaskCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_taskWithDeadline() {
        Task taskWithDeadline = new TaskBuilder()
                .withTaskName("Task 1")
                .withTaskDeadline("10-10-2020 10:00")
                .build();
        model.addTask(taskWithDeadline);
        Index noDeadlineTask = Index.fromOneBased(model.getTaskList().getSerializeTaskList().size());
        MarkTaskCommand markTaskCommand = new MarkTaskCommand(new Index[] { noDeadlineTask });

        String expectedMessage = String.format(MarkTaskCommand.MESSAGE_SUCCESS,
                Messages.format(taskWithDeadline));

        ModelManager expectedModel = new ModelManager(new AddressBook(), model.getTaskList(), new UserPrefs());

        assertCommandSuccess(markTaskCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        MarkTaskCommand markTaskFirstCommand = new MarkTaskCommand(new Index[] { INDEX_FIRST });
        MarkTaskCommand markTaskSecondCommand = new MarkTaskCommand(new Index[] { INDEX_SECOND });

        // same object -> returns true
        assertTrue(markTaskFirstCommand.equals(markTaskFirstCommand));

        // same values -> returns true
        MarkTaskCommand markTaskFirstCommandCopy = new MarkTaskCommand(new Index[] { INDEX_FIRST });
        assertTrue(markTaskFirstCommand.equals(markTaskFirstCommandCopy));

        // different types -> returns false
        assertFalse(markTaskFirstCommand.equals(1));

        // null -> returns false
        assertFalse(markTaskFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(markTaskFirstCommand.equals(markTaskSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index[] targetIndices = new Index[] {
                Index.fromOneBased(1),
                Index.fromOneBased(2)
        };
        MarkTaskCommand markTaskCommand = new MarkTaskCommand(targetIndices);
        String expected = MarkTaskCommand.class.getCanonicalName() + "{targetIndices="
                + Arrays.toString(targetIndices) + "}";
        assertEquals(expected, markTaskCommand.toString());
    }

}
