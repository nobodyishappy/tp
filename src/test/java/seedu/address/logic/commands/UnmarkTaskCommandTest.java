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
 * {@code UnmarkTaskCommand}.
 */
public class UnmarkTaskCommandTest {

    private Model model = new ModelManager(new AddressBook(), getTypicalTaskList(), new UserPrefs());

    @Test
    public void execute_singleValidIndex_success() {
        Task taskToUnmark = model.getTaskList().getSerializeTaskList().get(INDEX_FIRST.getZeroBased());
        UnmarkTaskCommand unmarkTaskCommand = new UnmarkTaskCommand(new Index[] { INDEX_FIRST });

        String expectedMessage = String.format(UnmarkTaskCommand.MESSAGE_SUCCESS,
                Messages.format(taskToUnmark));

        ModelManager expectedModel = new ModelManager(
                model.getAddressBook(), new TaskList(model.getTaskList()), new UserPrefs());
        expectedModel.setTask(taskToUnmark, new Task(
                taskToUnmark.getName(), taskToUnmark.getDescription(), taskToUnmark.getPriority(),
                new TaskStatus(), taskToUnmark.getDeadline()));

        assertCommandSuccess(unmarkTaskCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleValidIndex_success() {
        Task[] tasksToUnmark = new Task[] {
                model.getTaskList().getSerializeTaskList().get(INDEX_FIRST.getZeroBased()),
                model.getTaskList().getSerializeTaskList().get(INDEX_SECOND.getZeroBased())
        };

        UnmarkTaskCommand unmarkTaskCommand = new UnmarkTaskCommand(new Index[] { INDEX_FIRST, INDEX_SECOND });

        String expectedMessage = String.format(UnmarkTaskCommand.MESSAGE_SUCCESS,
                Messages.format(tasksToUnmark));

        ModelManager expectedModel = new ModelManager(
                model.getAddressBook(), new TaskList(model.getTaskList()), new UserPrefs());
        Arrays.stream(tasksToUnmark).forEach(taskToUnmark -> expectedModel.setTask(taskToUnmark, new Task(
                taskToUnmark.getName(),
                taskToUnmark.getDescription(),
                taskToUnmark.getPriority(),
                new TaskStatus(),
                taskToUnmark.getDeadline())));

        assertCommandSuccess(unmarkTaskCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateValidIndex_success() {
        Task taskToUnmark = model.getTaskList().getSerializeTaskList().get(INDEX_FIRST.getZeroBased());
        UnmarkTaskCommand unmarkTaskCommand = new UnmarkTaskCommand(new Index[] { INDEX_FIRST, INDEX_FIRST });

        String expectedMessage = String.format(UnmarkTaskCommand.MESSAGE_SUCCESS,
                Messages.format(taskToUnmark));

        ModelManager expectedModel = new ModelManager(
                model.getAddressBook(), new TaskList(model.getTaskList()), new UserPrefs());
        expectedModel.setTask(taskToUnmark, new Task(
                taskToUnmark.getName(), taskToUnmark.getDescription(), taskToUnmark.getPriority(),
                new TaskStatus(), taskToUnmark.getDeadline()));

        assertCommandSuccess(unmarkTaskCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_singleInvalidTaskIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getTaskList().getSerializeTaskList().size() + 1);
        UnmarkTaskCommand unmarkTaskCommand = new UnmarkTaskCommand(new Index[] { outOfBoundIndex });

        assertCommandFailure(unmarkTaskCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_duplicateInvalidTaskIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getTaskList().getSerializeTaskList().size() + 1);
        UnmarkTaskCommand unmarkTaskCommand = new UnmarkTaskCommand(new Index[] { outOfBoundIndex, outOfBoundIndex });

        assertCommandFailure(unmarkTaskCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_someInvalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getTaskList().getSerializeTaskList().size() + 1);
        UnmarkTaskCommand unmarkTaskCommand = new UnmarkTaskCommand(new Index[] { outOfBoundIndex, INDEX_FIRST });

        assertCommandFailure(unmarkTaskCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_allInvalidIndex_throwsCommandException() {
        UnmarkTaskCommand unmarkTaskCommand = new UnmarkTaskCommand(new Index[] {
                Index.fromOneBased(model.getTaskList().getSerializeTaskList().size() + 1),
                Index.fromOneBased(model.getTaskList().getSerializeTaskList().size() + 2)
        });

        assertCommandFailure(unmarkTaskCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_taskWithoutDeadline() {
        Task taskWithoutDeadline = new TaskBuilder().withTaskName("Task 1").withTaskDeadline("Empty").build();
        model.addTask(taskWithoutDeadline);
        Index noDeadlineTask = Index.fromOneBased(model.getTaskList().getSerializeTaskList().size());
        UnmarkTaskCommand unmarkTaskCommand = new UnmarkTaskCommand(new Index[] { noDeadlineTask });

        String expectedMessage = String.format(UnmarkTaskCommand.MESSAGE_SUCCESS,
                Messages.format(taskWithoutDeadline));

        ModelManager expectedModel = new ModelManager(new AddressBook(), model.getTaskList(), new UserPrefs());

        assertCommandSuccess(unmarkTaskCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_taskWithDeadline() {
        Task taskWithDeadline = new TaskBuilder()
                .withTaskName("Task 1")
                .withTaskDeadline("12-12-2024 16:00")
                .build();
        model.addTask(taskWithDeadline);
        Index deadlineTask = Index.fromOneBased(model.getTaskList().getSerializeTaskList().size());
        UnmarkTaskCommand unmarkTaskCommand = new UnmarkTaskCommand(new Index[] { deadlineTask });

        String expectedMessage = String.format(UnmarkTaskCommand.MESSAGE_SUCCESS,
                Messages.format(taskWithDeadline));

        ModelManager expectedModel = new ModelManager(new AddressBook(), model.getTaskList(), new UserPrefs());

        assertCommandSuccess(unmarkTaskCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        UnmarkTaskCommand unmarkTaskFirstCommand = new UnmarkTaskCommand(new Index[] { INDEX_FIRST });
        UnmarkTaskCommand unmarkTaskSecondCommand = new UnmarkTaskCommand(new Index[] { INDEX_SECOND });

        // same object -> returns true
        assertTrue(unmarkTaskFirstCommand.equals(unmarkTaskFirstCommand));

        // same values -> returns true
        UnmarkTaskCommand unmarkTaskFirstCommandCopy = new UnmarkTaskCommand(new Index[] { INDEX_FIRST });
        assertTrue(unmarkTaskFirstCommand.equals(unmarkTaskFirstCommandCopy));

        // different types -> returns false
        assertFalse(unmarkTaskFirstCommand.equals(1));

        // null -> returns false
        assertFalse(unmarkTaskFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(unmarkTaskFirstCommand.equals(unmarkTaskSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index[] targetIndices = new Index[] {
                Index.fromOneBased(1),
                Index.fromOneBased(2)
        };
        UnmarkTaskCommand unmarkTaskCommand = new UnmarkTaskCommand(targetIndices);
        String expected = UnmarkTaskCommand.class.getCanonicalName() + "{targetIndices="
                + Arrays.toString(targetIndices) + "}";
        assertEquals(expected, unmarkTaskCommand.toString());
    }
}
