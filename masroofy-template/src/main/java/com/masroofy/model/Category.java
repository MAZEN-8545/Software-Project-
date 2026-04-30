package com.masroofy.model;

/**
 * Represents a spending category that classifies transactions for reports and pie-chart
 * grouping in the dashboard (US#4).
 *
 * <p><b>OWNER: Mazen Mahmoud Abd El-Moez (20242258)</b></p>
 *
 * @version 1.0
 */
public class Category {

    // ── Fields ────────────────────────────────────────────────────
    private int categoryId;
    private CategoryType type;
    private String displayName;
    private String iconKey;

    /**
     * Creates a Category from a given type. Sets displayName automatically.
     *
     * @param type the {@link CategoryType} for this category
     */
    public Category(CategoryType type) {
        // TODO (Mazen): Set this.type = type; this.displayName = type.getDisplayName();
        throw new UnsupportedOperationException("Category(type) constructor not implemented yet — Mazen");
    }

    /**
     * Full constructor for loading from DB.
     *
     * @param categoryId unique DB identifier
     * @param type       category type
     * @param iconKey    icon resource key (used for future UI icons)
     */
    public Category(int categoryId, CategoryType type, String iconKey) {
        // TODO (Mazen): Assign all three fields; also set displayName = type.getDisplayName()
        throw new UnsupportedOperationException("Category(id, type, icon) constructor not implemented yet — Mazen");
    }

    // ── Getters & Setters ─────────────────────────────────────────
    // TODO (Mazen): Implement all getters and setters below.

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public CategoryType getType() { return type; }
    /** When type changes, also update the displayName automatically. */
    public void setType(CategoryType type) {
        // TODO (Mazen): this.type = type; this.displayName = type.getDisplayName();
        throw new UnsupportedOperationException("setType() not implemented yet — Mazen");
    }

    /**
     * Returns the human-readable category label shown in the UI and history list.
     *
     * @return display name string (e.g. "Food", "Transport")
     */
    public String getDisplayName() { return displayName; }

    public String getIconKey() { return iconKey; }
    public void setIconKey(String iconKey) { this.iconKey = iconKey; }
}
