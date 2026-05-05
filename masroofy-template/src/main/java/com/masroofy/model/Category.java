package com.masroofy.model;
public class Category {

    private int categoryId;
    private CategoryType type;
    private String displayName;
    private String iconKey;

    public Category(CategoryType type) {
        this.type        = type;
        this.displayName = type.getDisplayName();
    }

    public Category(int categoryId, CategoryType type, String iconKey) {
        this.categoryId  = categoryId;
        this.type        = type;
        this.iconKey     = iconKey;
        this.displayName = type.getDisplayName();
    }


    public int getCategoryId()              { return categoryId; }
    public void setCategoryId(int id)       { this.categoryId = id; }

    public CategoryType getType()           { return type; }

    public void setType(CategoryType type) {
        this.type        = type;
        this.displayName = type.getDisplayName();
    }

    public String getDisplayName()          { return displayName; }

    public String getIconKey()              { return iconKey; }
    public void setIconKey(String iconKey)  { this.iconKey = iconKey; }
}