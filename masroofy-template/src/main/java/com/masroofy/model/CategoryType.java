package com.masroofy.model;

/**
 * Enumeration of spending categories available in Masroofy.
 *
 * <p><b>OCP:</b> To add a new category (e.g. SUBSCRIPTIONS), add it here only.
 * No other class — no switch-case, no controller — needs to change.</p>
 *
 * <p><b>OWNER: Mazen Mahmoud Abd El-Moez (20242258)</b></p>
 *
 * @version 1.0
 */
public enum CategoryType {
    FOOD("Food"),
    TRANSPORT("Transport"),
    EDUCATION("Education"),
    HEALTH("Health"),
    ENTERTAINMENT("Entertainment"),
    SHOPPING("Shopping"),
    BILLS("Bills"),
    OTHER("Other");

    private final String displayName;

    CategoryType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the human-readable label for this category (shown in the UI).
     *
     * @return display name string, e.g. "Food"
     */
    public String getDisplayName() {
        return displayName;
    }
}
