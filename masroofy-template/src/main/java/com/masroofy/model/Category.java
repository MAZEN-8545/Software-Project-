package com.masroofy.model;

/**
 * Represents an expense category in the Masroofy system.
 * Wraps a {@link CategoryType} with optional database ID and icon key.
 *
 * @version 1.0
 */
public class Category {

    /** Database-generated unique identifier. */
    private int categoryId;
    /** The underlying category type (determines display name). */
    private CategoryType type;
    /** Human-readable name derived from the type. */
    private String displayName;
    /** Optional icon identifier for UI display. */
    private String iconKey;

    /**
     * Constructs a Category from a type (for new transactions).
     * The display name is automatically derived from the type.
     *
     * @param type the category type
     */
    public Category(CategoryType type) {
        this.type        = type;
        this.displayName = type.getDisplayName();
    }

    /**
     * Constructs a Category loaded from the database.
     *
     * @param categoryId the database ID
     * @param type the category type
     * @param iconKey the icon identifier
     */
    public Category(int categoryId, CategoryType type, String iconKey) {
        this.categoryId  = categoryId;
        this.type        = type;
        this.iconKey     = iconKey;
        this.displayName = type.getDisplayName();
    }

    /**
     * @return the category ID
     */
    public int getCategoryId()              { return categoryId; }

    /**
     * @param id the category ID to set
     */
    public void setCategoryId(int id)       { this.categoryId = id; }

    /**
     * @return the category type
     */
    public CategoryType getType()           { return type; }

    /**
     * Updates the category type and refreshes the display name.
     *
     * @param type the new category type
     */
    public void setType(CategoryType type) {
        this.type        = type;
        this.displayName = type.getDisplayName();
    }

    /**
     * @return the human-readable display name
     */
    public String getDisplayName()          { return displayName; }

    /**
     * @return the icon key
     */
    public String getIconKey()              { return iconKey; }

    /**
     * @param iconKey the icon key to set
     */
    public void setIconKey(String iconKey)  { this.iconKey = iconKey; }
}