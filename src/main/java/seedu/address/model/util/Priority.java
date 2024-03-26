package seedu.address.model.util;

/**
 * Contains enumeration for TaskPriority.
 */
public enum Priority {
    LOW(1),
    MEDIUM(2),
    HIGH(3);

    private int value;
    private Priority(int value) {
        this.value = value;
    }

    /**
     * Returns an int of {@code TaskPriority}.
     */
    public int getValue() {
        return value;
    }
}
