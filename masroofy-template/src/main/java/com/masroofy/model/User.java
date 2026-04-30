package com.masroofy.model;

/**
 * Represents the application owner profile.
 * A User owns one or more {@link BudgetCycle}s.
 *
 * <p>Masroofy is a single-user app, so userId is always 1.</p>
 *
 * <p><b>OWNER: Mazen Mahmoud Abd El-Moez (20242258)</b></p>
 *
 * @version 1.0
 */
public class User {

    private int userId;
    private String name;
    private int age;

    /**
     * Default constructor. Sets userId = 1 and name = "User".
     * Used when the default user is seeded in the DB.
     */
    public User() {
        // TODO (Mazen): Set this.userId = 1; this.name = "User";
        throw new UnsupportedOperationException("User() constructor not implemented yet — Mazen");
    }

    /**
     * Creates a User with all fields.
     *
     * @param userId unique identifier (always 1 for single-user app)
     * @param name   display name
     * @param age    user's age
     */
    public User(int userId, String name, int age) {
        // TODO (Mazen): Assign all three fields.
        throw new UnsupportedOperationException("User(id,name,age) constructor not implemented yet — Mazen");
    }

    // ── Getters & Setters ─────────────────────────────────────────
    // TODO (Mazen): Implement standard getters and setters.

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}
