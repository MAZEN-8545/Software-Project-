package com.masroofy.model;

/**
 * Represents a user in the Masroofy budget management system.
 * Currently supports a single default user (userId=1).
 *
 * @version 1.0
 */
public class User {

    /** Unique identifier for the user. */
    private int userId;
    /** Display name of the user. */
    private String name;
    /** Age of the user (optional). */
    private int age;

    /**
     * Default constructor — creates the default user with ID=1 and name="User".
     */
    public User() {
        this.userId = 1;
        this.name   = "User";
    }

    /**
     * Constructs a User with specified attributes.
     *
     * @param userId the unique user identifier
     * @param name the display name
     * @param age the user's age
     */
    public User(int userId, String name, int age) {
        this.userId = userId;
        this.name   = name;
        this.age    = age;
    }

    /**
     * @return the user ID
     */
    public int getUserId()              { return userId; }

    /**
     * @param userId the user ID to set
     */
    public void setUserId(int userId)   { this.userId = userId; }

    /**
     * @return the user's name
     */
    public String getName()             { return name; }

    /**
     * @param name the name to set
     */
    public void setName(String name)    { this.name = name; }

    /**
     * @return the user's age
     */
    public int getAge()                 { return age; }

    /**
     * @param age the age to set
     */
    public void setAge(int age)         { this.age = age; }
}