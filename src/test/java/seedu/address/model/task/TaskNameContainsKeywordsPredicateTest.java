package seedu.address.model.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.TaskBuilder;

class TaskNameContainsKeywordsPredicateTest {
    @Test
    void test_taskNameContainsKeywords_returnsTrue() {
        // One keyword
        TaskNameContainsKeywordsPredicate predicate =
                new TaskNameContainsKeywordsPredicate(Collections.singletonList("2101"));
        assertTrue(predicate.test(new TaskBuilder().withTaskName("2101").build()));

        // Multiple keywords
        predicate = new TaskNameContainsKeywordsPredicate(Arrays.asList("2101", "2103t"));
        assertTrue(predicate.test(new TaskBuilder().withTaskName("2101 2103t").build()));

        // Only one matching keyword
        predicate = new TaskNameContainsKeywordsPredicate(Arrays.asList("2101", "2103"));
        assertTrue(predicate.test(new TaskBuilder().withTaskName("2101 2103t").build()));

        // Mixed-case keywords
        predicate = new TaskNameContainsKeywordsPredicate(Arrays.asList("2109S", "2103T"));
        assertTrue(predicate.test(new TaskBuilder().withTaskName("2103t 2109s").build()));
    }

    @Test
    public void test_taskNameDoesNotContainKeywords_returnsFalse() {
        // Zero keyword
        TaskNameContainsKeywordsPredicate predicate = new TaskNameContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new TaskBuilder().withTaskName("2103t").build()));

        // Non-matching keyword
        predicate = new TaskNameContainsKeywordsPredicate(Arrays.asList("2101"));
        assertFalse(predicate.test(new TaskBuilder().withTaskName("2103t").build()));

        // Keywords match description, but does not match name
        predicate = new TaskNameContainsKeywordsPredicate(Arrays.asList("dummy"));
        assertFalse(predicate.test(new TaskBuilder().withTaskName("2103t")
                .withTaskDescription("dummy").build()));
    }

    @Test
    void testEquals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        TaskNameContainsKeywordsPredicate firstPredicate =
                new TaskNameContainsKeywordsPredicate(firstPredicateKeywordList);
        TaskNameContainsKeywordsPredicate secondPredicate =
                new TaskNameContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        TaskNameContainsKeywordsPredicate firstPredicateCopy =
                new TaskNameContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different values -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    void testToString() {
        List<String> keywords = List.of("keyword1", "keyword2");
        TaskNameContainsKeywordsPredicate predicate = new TaskNameContainsKeywordsPredicate(keywords);

        String expected = TaskNameContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }

}
