package com.masroofy.repository;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles SQLite database connection and schema initialisation.
 *
 * <p><b>Important — DB path:</b> Use {@code System.getProperty("user.home")} to find a safe,
 * always-available directory. Do NOT use {@code getCodeSource().getLocation()} as it can return
 * {@code null} under IntelliJ/Maven and crash silently. The DB file lives at:
 * <pre>~/.masroofy/masroofy.db</pre></p>
 *
 * <p><b>Tables to create (if not exists):</b>
 * <ul>
 *   <li>{@code users} — user_id (PK), name, age</li>
 *   <li>{@code budget_cycles} — cycle_id (PK AUTOINCREMENT), user_id (FK), total_allowance,
 *       start_date, end_date, remaining_balance, safe_daily_limit, active (0/1)</li>
 *   <li>{@code transactions} — transaction_id (PK AUTOINCREMENT), cycle_id (FK), amount,
 *       note, category_type (TEXT), timestamp (TEXT)</li>
 * </ul>
 * </p>
 *
 * <p><b>OWNER: Mahmoud Mokhtar Mohamed (20242320)</b></p>
 *
 * @version 1.0
 */
public class DatabaseHelper {

    /** JDBC URL pointing to the SQLite file in the user's home directory. */
    private static final String DB_URL;

    static {
        // TODO (Mahmoud Mokhtar): Build DB_URL here.
        // 1. File dir = new File(System.getProperty("user.home"), ".masroofy");
        // 2. if (!dir.exists()) dir.mkdirs();
        // 3. File dbFile = new File(dir, "masroofy.db");
        // 4. DB_URL = "jdbc:sqlite:" + dbFile.getAbsolutePath().replace("\\", "/");
        // 5. Print the path with System.out.println("[DB] Database: " + dbFile.getAbsolutePath());
        DB_URL = null; // Replace this line with real implementation
    }

    /**
     * Opens and returns a new JDBC connection to the SQLite database.
     *
     * @return a live {@link Connection}
     * @throws SQLException if the driver fails or the file cannot be opened
     */
    public static Connection connect() throws SQLException {
        // TODO (Mahmoud Mokhtar): return DriverManager.getConnection(DB_URL);
        throw new UnsupportedOperationException("connect() not implemented yet — Mahmoud Mokhtar");
    }

    /**
     * Creates all tables if they do not already exist. Called once at startup by
     * {@link LocalStorageRepository} constructor.
     *
     * <p>Use {@code CREATE TABLE IF NOT EXISTS} so repeated calls are safe.</p>
     */
    public static void initializeDatabase() {
        // TODO (Mahmoud Mokhtar): Open connect() + Statement, then execute three CREATE TABLE IF NOT EXISTS
        // statements for: users, budget_cycles, transactions.
        // Wrap in try-with-resources. Print success/failure messages.
        // See SDS class diagram for exact column names and types.
        throw new UnsupportedOperationException("initializeDatabase() not implemented yet — Mahmoud Mokhtar");
    }
}
