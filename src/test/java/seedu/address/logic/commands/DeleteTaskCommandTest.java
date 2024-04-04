package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalTasks.getTypicalTaskList;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.TaskList;
import seedu.address.model.UserPrefs;
import seedu.address.model.task.Task;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteTaskCommand}.
 */
public class DeleteTaskCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), getTypicalTaskList(), new UserPrefs());

    @Test
    public void execute_singleValidIndex_success() {
        Task taskToDelete = model.getTaskList().getSerializeTaskList().get(INDEX_FIRST.getZeroBased());
        DeleteTaskCommand deleteTaskCommand = new DeleteTaskCommand(new Index[] { INDEX_FIRST });

        String expectedMessage = String.format(DeleteTaskCommand.MESSAGE_DELETE_TASKS_SUCCESS,
                Messages.format(taskToDelete));

        ModelManager expectedModel = new ModelManager(
                model.getAddressBook(), new TaskList(model.getTaskList()), new UserPrefs());
        expectedModel.deleteTask(taskToDelete);

        assertCommandSuccess(deleteTaskCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleValidIndex_success() {
        Task[] tasksToDelete = new Task[] {
                model.getTaskList().getSerializeTaskList().get(INDEX_FIRST.getZeroBased()),
                model.getTaskList().getSerializeTaskList().get(INDEX_SECOND.getZeroBased())
        };

        DeleteTaskCommand deleteTaskCommand = new DeleteTaskCommand(new Index[] { INDEX_FIRST, INDEX_SECOND });

        String expectedMessage = String.format(DeleteTaskCommand.MESSAGE_DELETE_TASKS_SUCCESS,
                Messages.format(tasksToDelete));

        ModelManager expectedModel = new ModelManager(
                model.getAddressBook(), new TaskList(model.getTaskList()), new UserPrefs());
        expectedModel.deleteTask(tasksToDelete[0]);
        expectedModel.deleteTask(tasksToDelete[1]);

        assertCommandSuccess(deleteTaskCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateValidIndex_success() {
        Task taskToDelete = model.getTaskList().getSerializeTaskList().get(INDEX_FIRST.getZeroBased());
        DeleteTaskCommand deleteTaskCommand = new DeleteTaskCommand(new Index[] { INDEX_FIRST, INDEX_FIRST });

        String expectedMessage = String.format(DeleteTaskCommand.MESSAGE_DELETE_TASKS_SUCCESS,
                Messages.format(taskToDelete));

        ModelManager expectedModel = new ModelManager(
                model.getAddressBook(), new TaskList(model.getTaskList()), new UserPrefs());
        expectedModel.deleteTask(taskToDelete);

        assertCommandSuccess(deleteTaskCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_singleInvalidTaskIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getTaskList().getSerializeTaskList().size() + 1);
        DeleteTaskCommand deleteTaskCommand = new DeleteTaskCommand(new Index[] { outOfBoundIndex });

        assertCommandFailure(deleteTaskCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_duplicateInvalidTaskIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getTaskList().getSerializeTaskList().size() + 1);
        DeleteTaskCommand deleteTaskCommand = new DeleteTaskCommand(new Index[] { outOfBoundIndex, outOfBoundIndex });

        assertCommandFailure(deleteTaskCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_someInvalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getTaskList().getSerializeTaskList().size() + 1);
        DeleteTaskCommand deleteTaskCommand = new DeleteTaskCommand(new Index[] { outOfBoundIndex, INDEX_FIRST });

        assertCommandFailure(deleteTaskCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_allInvalidIndex_throwsCommandException() {
        DeleteTaskCommand deleteTaskCommand = new DeleteTaskCommand(new Index[] {
                Index.fromOneBased(model.getTaskList().getSerializeTaskList().size() + 1),
                Index.fromOneBased(model.getTaskList().getSerializeTaskList().size() + 2)
        });

        assertCommandFailure(deleteTaskCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeleteTaskCommand deleteFirstCommand = new DeleteTaskCommand(new Index[] { INDEX_FIRST });
        DeleteTaskCommand deleteSecondCommand = new DeleteTaskCommand(new Index[] { INDEX_SECOND });

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteTaskCommand deleteFirstCommandCopy = new DeleteTaskCommand(new Index[] { INDEX_FIRST });
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index[] targetIndices = new Index[] {
                Index.fromOneBased(1),
                Index.fromOneBased(2)
        };
        DeleteTaskCommand deleteTaskCommand = new DeleteTaskCommand(targetIndices);
        String expected = DeleteTaskCommand.class.getCanonicalName() + "{targetIndices="
                + Arrays.toString(targetIndices) + "}";
        assertEquals(expected, deleteTaskCommand.toString());
    }
}
