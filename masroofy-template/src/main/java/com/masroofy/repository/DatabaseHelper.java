package com.masroofy.repository;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles SQLite database connection and schema initialisation.
 * 
 *<b>OWNER: Mahmoud Mokhtar Mohamed (20242320)</b>
 *
 * @version 1.0
 */
public class DatabaseHelper {

    /** JDBC URL pointing to the SQLite file in the user's home directory. */
    private static final String DB_URL;

    static {
        File dir = new File(System.getProperty("user.home"), ".masroofy");
        if (!dir.exists()) dir.mkdirs();
        File dbFile = new File(dir, "masroofy.db");
        DB_URL = "jdbc:sqlite:" + dbFile.getAbsolutePath().replace("\\", "/");
        System.out.println("[DB] Database: " + dbFile.getAbsolutePath());
    }

    /**
     * Opens and returns a new JDBC connection to the SQLite database.
     *
     * @return a live {@link Connection}
     * @throws SQLException if the driver fails or the file cannot be opened
     */
    public static Connection connect() throws SQLException {
      return DriverManager.getConnection(DB_URL);
    }

    /**
     * Creates all tables if they do not already exist. Called once at startup by
     * {@link LocalStorageRepository} constructor.
     *
     * <p>Use {@code CREATE TABLE IF NOT EXISTS} so repeated calls are safe.</p>
     */
    public static void initializeDatabase() {
       try (Connection conn = connect(); Statement stmt = conn.createStatement()) {

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS users (
                        user_id   INTEGER PRIMARY KEY,
                        name      TEXT    NOT NULL,
                        age       INTEGER
                    )
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS budget_cycles (
                        cycle_id          INTEGER PRIMARY KEY AUTOINCREMENT,
                        user_id           INTEGER NOT NULL,
                        total_allowance   REAL    NOT NULL,
                        start_date        TEXT    NOT NULL,
                        end_date          TEXT    NOT NULL,
                        remaining_balance REAL    NOT NULL,
                        safe_daily_limit  REAL    NOT NULL,
                        active            INTEGER NOT NULL DEFAULT 1,
                        FOREIGN KEY (user_id) REFERENCES users(user_id)
                    )
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS transactions (
                        transaction_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        cycle_id       INTEGER NOT NULL,
                        amount         REAL    NOT NULL,
                        note           TEXT,
                        category_type  TEXT    NOT NULL,
                        timestamp      TEXT    NOT NULL,
                        FOREIGN KEY (cycle_id) REFERENCES budget_cycles(cycle_id)
                    )
                    """);

            // US#12 — Privacy Lock table for local authentication
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS privacy_lock (
                        lock_id      INTEGER PRIMARY KEY DEFAULT 1,
                        enabled      INTEGER NOT NULL DEFAULT 0,
                        pin_hash     TEXT,
                        updated_at   TEXT    NOT NULL DEFAULT CURRENT_TIMESTAMP
                    )
                    """);

            // Insert default row if not exists (single row table)
            stmt.execute("""
                    INSERT OR IGNORE INTO privacy_lock (lock_id, enabled, updated_at)
                    VALUES (1, 0, CURRENT_TIMESTAMP)
                    """);

            System.out.println("[DB] Schema initialised successfully.");

        } catch (SQLException e) {
            System.err.println("[DB] Initialisation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
