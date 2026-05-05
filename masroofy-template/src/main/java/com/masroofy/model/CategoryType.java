package com.masroofy.model;

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

    public String getDisplayName() {
        return displayName;
    }
}
