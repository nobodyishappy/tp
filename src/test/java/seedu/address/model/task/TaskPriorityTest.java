package seedu.address.model.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TaskPriorityTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TaskPriority(null));
    }

    @Test
    public void isValidTaskPriority() {
        // null description
        assertThrows(NullPointerException.class, () -> TaskDescription.isValidTaskDescription(null));

        // invalid description
        assertFalse(TaskPriority.isValidTaskPriority("")); // empty string
        assertFalse(TaskPriority.isValidTaskPriority(" ")); // spaces only

        // valid description
        assertTrue(TaskPriority.isValidTaskPriority("low")); // alphabets only
        assertTrue(TaskPriority.isValidTaskPriority("1")); // numbers only
        assertTrue(TaskPriority.isValidTaskPriority("HIGH")); // Capital letters
        assertTrue(TaskPriority.isValidTaskPriority("High")); // Mix of upper and lowercase letters
    }

    @Test
    public void toStringTest() {
        TaskPriority taskPriority1 = new TaskPriority("1");
        TaskPriority taskPriority2 = new TaskPriority("LOW");

        assertEquals("LOW", taskPriority1.toString());
        assertEquals("LOW", taskPriority2.toString());
    }

    @Test
    public void equals() {
        TaskPriority taskPriority = new TaskPriority("1");

        // same values -> returns true
        assertTrue(taskPriority.equals(new TaskPriority("1")));

        // same object -> returns true
        assertTrue(taskPriority.equals(taskPriority));

        // null -> returns false
        assertFalse(taskPriority.equals(null));

        // different types -> returns false
        assertFalse(taskPriority.equals(5.0f));

        // different values -> returns false
        assertFalse(taskPriority.equals(new TaskPriority("HIGH")));
    }
}
