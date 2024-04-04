package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.task.Task;

/**
 * Assigns the task identified using its displayed index to
 * the people identified using their displayed index in the address book.
 */
public class AssignCommand extends Command {

    public static final String COMMAND_WORD = "assign";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Assigns the task identified "
            + "by the index number used in the last task listing "
            + "to the people identified "
            + "by the index numbers used in the last person listing. "
            + "Does nothing if the task is already assigned to a person.\n"
            + "Parameters: TASK_INDEX (must be a positive integer) "
            + "to/ PERSON_INDEX [MORE_PERSON_INDICES] (must be distinct positive integers)\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + "to/ 1 2";

    public static final String MESSAGE_SUCCESS = "%1$s has been assigned to %2$s.";

    private final Index taskIndex;
    private final Index[] personIndices;

    /**
     * @param taskIndex of the task in the filtered task list to be assigned to the person
     * @param personIndices of the people in the filtered person list to be assigned the task
     */
    public AssignCommand(Index taskIndex, Index[] personIndices) {
        requireAllNonNull(taskIndex, personIndices);

        this.taskIndex = taskIndex;
        this.personIndices = personIndices;
    }

    private void verifyAllTaskIndicesWithinRange(Index[] taskIndices, int range) throws CommandException {
        if (Arrays.stream(taskIndices).anyMatch(targetIndex -> targetIndex.getZeroBased() >= range)) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
    }

    private void verifyAllPersonIndicesWithinRange(Index[] personIndices, int range) throws CommandException {
        if (Arrays.stream(personIndices).anyMatch(personIndex -> personIndex.getZeroBased() >= range)) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
    }

    private Task getTaskToAssign(Model model) throws CommandException {
        // Use filtered list
        List<Task> lastShownTaskList = model.getFilteredTaskList();
        verifyAllTaskIndicesWithinRange(new Index[] { taskIndex }, lastShownTaskList.size());

        return lastShownTaskList.get(taskIndex.getZeroBased());
    }

    private Person[] getPeopleToBeAssigned(Model model) throws CommandException {
        List<Person> lastShownPersonList = model.getFilteredPersonList();
        verifyAllPersonIndicesWithinRange(personIndices, lastShownPersonList.size());

        return Arrays.stream(personIndices).distinct()
                .map(targetIndex -> lastShownPersonList.get(targetIndex.getZeroBased()))
                .toArray(Person[]::new);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Task taskToAssign = getTaskToAssign(model);
        Person[] peopleToBeAssigned = getPeopleToBeAssigned(model);

        Arrays.stream(peopleToBeAssigned).forEach(personToBeAssigned -> model.setPerson(
                personToBeAssigned, personToBeAssigned.addTask(taskToAssign)));

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(taskToAssign),
                Messages.format(peopleToBeAssigned)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AssignCommand)) {
            return false;
        }

        AssignCommand e = (AssignCommand) other;
        return taskIndex.equals(e.taskIndex)
                && Arrays.equals(personIndices, e.personIndices);
    }
}
