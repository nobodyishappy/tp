package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalTasks.getTypicalTaskList;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditTaskCommand.EditTaskDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.task.Task;
import seedu.address.testutil.EditTaskDescriptorBuilder;
import seedu.address.testutil.TaskBuilder;

public class EditTaskCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), getTypicalTaskList(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Task target = model.getFilteredTaskList().get(0);
        Task editedTask = new TaskBuilder().withTaskName("task4").build();

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder(editedTask).build();
        EditTaskCommand editTaskCommand = new EditTaskCommand(INDEX_FIRST, descriptor);

        String expectedMessage = String.format(EditTaskCommand.MESSAGE_SUCCESS,
                Messages.format(target));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()),
                getTypicalTaskList(), new UserPrefs());
        expectedModel.setTask(target, editedTask);

        assertCommandSuccess(editTaskCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastTask = Index.fromOneBased(model.getFilteredTaskList().size());
        Task lastTask = model.getFilteredTaskList().get(indexLastTask.getZeroBased());

        TaskBuilder taskInList = new TaskBuilder(lastTask);
        Task editedTask = taskInList.withTaskName("Task 10").withTaskPriority("medium")
                .build();

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withName("Task 10")
                .withPriority("medium").build();
        EditTaskCommand editTaskCommand = new EditTaskCommand(indexLastTask, descriptor);

        String expectedMessage = String.format(EditTaskCommand.MESSAGE_SUCCESS,
                Messages.format(lastTask));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()),
                getTypicalTaskList(), new UserPrefs());
        expectedModel.setTask(lastTask, editedTask);

        assertCommandSuccess(editTaskCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditTaskCommand editTaskCommand = new EditTaskCommand(INDEX_FIRST, new EditTaskDescriptor());
        Task editedTask = model.getFilteredTaskList().get(INDEX_FIRST.getZeroBased());

        String expectedMessage = String.format(EditTaskCommand.MESSAGE_SUCCESS,
                Messages.format(editedTask));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()),
                model.getTaskList(), new UserPrefs());

        assertCommandSuccess(editTaskCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Task firstTask = model.getFilteredTaskList().get(INDEX_FIRST.getZeroBased());
        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder(firstTask).build();
        EditTaskCommand editTaskCommand = new EditTaskCommand(INDEX_SECOND, descriptor);

        assertCommandFailure(editTaskCommand, model, EditTaskCommand.MESSAGE_DUPLICATE_TASK);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTaskList().size() + 1);
        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withName("task 11").build();
        EditTaskCommand editTaskCommand = new EditTaskCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editTaskCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Task task4 = new TaskBuilder().withTaskName("task4").build();
        Task task5 = new TaskBuilder().withTaskName("task5").build();
        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder(task4).build();
        EditTaskDescriptor descriptor2 = new EditTaskDescriptorBuilder(task5).build();

        final EditTaskCommand standardCommand = new EditTaskCommand(INDEX_FIRST, descriptor);

        // same values -> returns true
        EditTaskDescriptor copyDescriptor = new EditTaskDescriptor(descriptor);
        EditTaskCommand commandWithSameValues = new EditTaskCommand(INDEX_FIRST, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditTaskCommand(INDEX_SECOND, descriptor)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditTaskCommand(INDEX_FIRST, descriptor2)));
    }
}
