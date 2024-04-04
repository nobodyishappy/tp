package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalTasks.getTypicalTaskList;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.TaskList;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.task.Task;
import seedu.address.testutil.PersonBuilder;

class UnassignCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), getTypicalTaskList(), new UserPrefs());

    @Test
    public void execute_unassignTaskUnfilteredListSingleValidIndex_success() {
        Task taskToUnassign = model.getTaskList().getSerializeTaskList().get(INDEX_FIRST.getZeroBased());

        Person personToBeUnassigned = model.getFilteredPersonList().get(INDEX_FIRST.getZeroBased());

        UnassignCommand unassignCommand = new UnassignCommand(INDEX_FIRST, new Index[] { INDEX_FIRST });

        String expectedMessage = String.format(UnassignCommand.MESSAGE_SUCCESS,
                Messages.format(taskToUnassign), Messages.format(new Person[] { personToBeUnassigned }));

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new TaskList(model.getTaskList()), new UserPrefs());
        Set<Task> editedTasks = new HashSet<>(personToBeUnassigned.getTasks());
        editedTasks.remove(taskToUnassign);
        expectedModel.setPerson(personToBeUnassigned,
                new PersonBuilder(personToBeUnassigned).withTasks(editedTasks).build());

        assertCommandSuccess(unassignCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_unassignTaskUnfilteredListMultipleValidIndex_success() {
        Task taskToUnassign = model.getTaskList().getSerializeTaskList().get(INDEX_FIRST.getZeroBased());

        Person[] peopleToBeUnassigned = new Person[] {
                model.getFilteredPersonList().get(INDEX_FIRST.getZeroBased()),
                model.getFilteredPersonList().get(INDEX_SECOND.getZeroBased())
        };

        UnassignCommand unassignCommand = new UnassignCommand(INDEX_FIRST, new Index[] { INDEX_FIRST, INDEX_SECOND });

        String expectedMessage = String.format(UnassignCommand.MESSAGE_SUCCESS,
                Messages.format(taskToUnassign), Messages.format(peopleToBeUnassigned));

        ModelManager expectedModel = new ModelManager(
                model.getAddressBook(), new TaskList(model.getTaskList()), new UserPrefs());
        Arrays.stream(peopleToBeUnassigned).forEach(personToBeUnassigned -> {
            Set<Task> editedTasks = new HashSet<>(personToBeUnassigned.getTasks());
            editedTasks.remove(taskToUnassign);
            expectedModel.setPerson(personToBeUnassigned,
                    new PersonBuilder(personToBeUnassigned).withTasks(editedTasks).build());
        });

        assertCommandSuccess(unassignCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_unassignTaskUnfilteredListDuplicateValidIndex_success() {
        Task taskToUnassign = model.getTaskList().getSerializeTaskList().get(INDEX_FIRST.getZeroBased());

        Person personToBeUnassigned = model.getFilteredPersonList().get(0);

        UnassignCommand unassignCommand = new UnassignCommand(INDEX_FIRST, new Index[] { INDEX_FIRST, INDEX_FIRST });

        String expectedMessage = String.format(UnassignCommand.MESSAGE_SUCCESS,
                Messages.format(taskToUnassign), Messages.format(new Person[] {personToBeUnassigned}));

        ModelManager expectedModel = new ModelManager(
                model.getAddressBook(), new TaskList(model.getTaskList()), new UserPrefs());
        Set<Task> editedTasks = new HashSet<>(personToBeUnassigned.getTasks());
        editedTasks.remove(taskToUnassign);
        expectedModel.setPerson(personToBeUnassigned,
                new PersonBuilder(personToBeUnassigned).withTasks(editedTasks).build());

        assertCommandSuccess(unassignCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST.getZeroBased());
        Set<Task> editedTasks = new HashSet<>(firstPerson.getTasks());
        Task taskToUnassign = model.getTaskList().getSerializeTaskList().get(INDEX_FIRST.getZeroBased());
        editedTasks.remove(taskToUnassign);
        Person editedPerson = new PersonBuilder(firstPerson).withTasks(editedTasks).build();

        UnassignCommand unassignCommand = new UnassignCommand(INDEX_FIRST, new Index[] { INDEX_FIRST });

        String expectedMessage = String.format(UnassignCommand.MESSAGE_SUCCESS, Messages.format(taskToUnassign),
                editedPerson.getName());

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new TaskList(model.getTaskList()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(unassignCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidTaskIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getTaskList().getSerializeTaskList().size() + 1);
        UnassignCommand unassignCommand = new UnassignCommand(outOfBoundIndex, new Index[] { INDEX_FIRST });

        assertCommandFailure(unassignCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_singleInvalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        UnassignCommand unassignCommand = new UnassignCommand(INDEX_FIRST, new Index[] { outOfBoundIndex });

        assertCommandFailure(unassignCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_duplicateInvalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        UnassignCommand unassignCommand = new UnassignCommand(INDEX_FIRST,
                new Index[] { outOfBoundIndex, outOfBoundIndex });

        assertCommandFailure(unassignCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_someInvalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        UnassignCommand unassignCommand = new UnassignCommand(INDEX_FIRST,
                new Index[] { outOfBoundIndex, INDEX_FIRST });

        assertCommandFailure(unassignCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_allInvalidIndex_throwsCommandException() {
        UnassignCommand unassignCommand = new UnassignCommand(INDEX_FIRST, new Index[] {
                Index.fromOneBased(model.getFilteredPersonList().size() + 1),
                Index.fromOneBased(model.getFilteredPersonList().size() + 2)
        });

        assertCommandFailure(unassignCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST);
        Index outOfBoundIndex = INDEX_SECOND;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        UnassignCommand unassignCommand = new UnassignCommand(INDEX_FIRST, new Index[] { outOfBoundIndex });

        assertCommandFailure(unassignCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        UnassignCommand unassignCommand = new UnassignCommand(INDEX_FIRST, new Index[] { INDEX_FIRST });
        UnassignCommand unassignOneToTwoCommand = new UnassignCommand(INDEX_FIRST, new Index[] { INDEX_SECOND });
        UnassignCommand unassignTwoToOneCommand = new UnassignCommand(INDEX_SECOND, new Index[] { INDEX_FIRST });

        // same object -> returns true
        assertEquals(unassignCommand, unassignCommand);

        // same values -> returns true
        UnassignCommand unassignCommandCopy = new UnassignCommand(INDEX_FIRST, new Index[] { INDEX_FIRST });
        assertEquals(unassignCommand, unassignCommandCopy);

        // null -> returns false
        assertNotEquals(unassignCommand, null);

        // different indices -> returns false
        assertNotEquals(unassignCommand, unassignOneToTwoCommand);
        assertNotEquals(unassignCommand, unassignTwoToOneCommand);
    }
}
