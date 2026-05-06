package com.masroofy.model;

/**
 * Enum representing all expense categories available in Masroofy.
 * Each category has a human-readable display name.
 *
 * @version 1.0
 */
public enum CategoryType {
    /** Food and dining expenses. */
    FOOD("Food"),
    /** Transportation costs (fuel, public transit, etc.). */
    TRANSPORT("Transport"),
    /** Educational expenses (books, courses, etc.). */
    EDUCATION("Education"),
    /** Medical and health-related expenses. */
    HEALTH("Health"),
    /** Entertainment and leisure spending. */
    ENTERTAINMENT("Entertainment"),
    /** Shopping expenses (clothing, electronics, etc.). */
    SHOPPING("Shopping"),
    /** Monthly bills and utilities. */
    BILLS("Bills"),
    /** Miscellaneous uncategorized expenses. */
    OTHER("Other");

    /** The human-readable name for this category. */
    private final String displayName;

    /**
     * Constructs a category type with the specified display name.
     *
     * @param displayName the human-readable name
     */
    CategoryType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the human-readable display name for this category.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
}
