package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.TaskList;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new TaskList(), new UserPrefs());

    @Test
    public void execute_singleValidIndexUnfilteredList_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(new Index[] { INDEX_FIRST });

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PEOPLE_SUCCESS,
                Messages.format(new Person[] { personToDelete }));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new TaskList(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleValidIndexUnfilteredList_success() {
        Person[] peopleToDelete = new Person[] {
                model.getFilteredPersonList().get(INDEX_FIRST.getZeroBased()),
                model.getFilteredPersonList().get(INDEX_SECOND.getZeroBased())
        };
        DeleteCommand deleteCommand = new DeleteCommand(new Index[] { INDEX_FIRST, INDEX_SECOND });

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PEOPLE_SUCCESS,
                Messages.format(peopleToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new TaskList(), new UserPrefs());
        expectedModel.deletePerson(peopleToDelete[0]);
        expectedModel.deletePerson(peopleToDelete[1]);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateValidIndexUnfilteredList_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(new Index[] { INDEX_FIRST, INDEX_FIRST});

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PEOPLE_SUCCESS,
                Messages.format(new Person[] { personToDelete }));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new TaskList(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_singleInvalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(new Index[] { outOfBoundIndex });

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_duplicateInvalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(new Index[] { outOfBoundIndex, outOfBoundIndex });

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_someInvalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(new Index[] { outOfBoundIndex, INDEX_FIRST });

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_allInvalidIndexUnfilteredList_throwsCommandException() {
        DeleteCommand deleteCommand = new DeleteCommand(new Index[] {
                Index.fromOneBased(model.getFilteredPersonList().size() + 1),
                Index.fromOneBased(model.getFilteredPersonList().size() + 2)
        });

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST);

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(new Index[] { INDEX_FIRST });

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PEOPLE_SUCCESS,
                Messages.format(new Person[] { personToDelete }));

        Model expectedModel = new ModelManager(model.getAddressBook(), new TaskList(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        showNoPerson(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST);

        Index outOfBoundIndex = INDEX_SECOND;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        DeleteCommand deleteCommand = new DeleteCommand(new Index[] { outOfBoundIndex });

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(new Index[] { INDEX_FIRST });
        DeleteCommand deleteSecondCommand = new DeleteCommand(new Index[] { INDEX_SECOND });

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(new Index[] { INDEX_FIRST });
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
        DeleteCommand deleteCommand = new DeleteCommand(targetIndices);
        String expected = DeleteCommand.class.getCanonicalName() + "{targetIndices="
                + Arrays.toString(targetIndices) + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
