package com.masroofy.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.masroofy.model.BudgetCycle;
import com.masroofy.model.Category;
import com.masroofy.model.CategoryType;
import com.masroofy.model.Transaction;
import com.masroofy.util.DateUtils;

/**
 * Singleton repository — all SQLite CRUD for Masroofy.
 *
 * <p><b>OWNER: Mahmoud Mokhtar Mohamed (20242320)</b></p>
 *
 * @version 1.0
 */
public class LocalStorageRepository {

    /** The single instance (Singleton pattern). */
    private static LocalStorageRepository instance;

    /**
     * Private constructor — Singleton.
     * Calls {@link DatabaseHelper#initializeDatabase()} and seeds the default user.
     */
    private LocalStorageRepository() {
         DatabaseHelper.initializeDatabase();
        seedDefaultUser();
    }

    /**
     * Returns the single {@link LocalStorageRepository} instance, creating it on first call.
     *
     * @return the singleton instance
     */
    public static LocalStorageRepository getInstance() {
        if (instance == null) instance = new LocalStorageRepository();
        return instance;
    }

    // ── Seed ──────────────────────────────────────────────────────

    /**
     * Inserts the default user (user_id=1) if it does not already exist.
     * Uses {@code INSERT OR IGNORE} so it is safe to call repeatedly.
     */
    private void seedDefaultUser() {
         String check  = "SELECT COUNT(*) FROM users WHERE user_id = 1";
        String insert = "INSERT OR IGNORE INTO users(user_id, name, age) VALUES(1,'User',0)";
        try (Connection conn = DatabaseHelper.connect();
             Statement  stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(check);
            if (rs.next() && rs.getInt(1) == 0) stmt.execute(insert);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // ── BudgetCycle ───────────────────────────────────────────────

    /**
     *
     * @param cycle the new cycle to persist (cycleId will be set after save)
     */
    public void saveCycle(BudgetCycle cycle) {
       String deactivate = "UPDATE budget_cycles SET active = 0 WHERE user_id = 1 AND active = 1";
        String insert = """
                INSERT INTO budget_cycles
                (user_id, total_allowance, start_date, end_date, remaining_balance, safe_daily_limit, active)
                VALUES (?, ?, ?, ?, ?, ?, 1)
                """;
        try (Connection conn = DatabaseHelper.connect()) {
            conn.setAutoCommit(false);
            try (Statement  deact = conn.createStatement();
                 PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {

                deact.execute(deactivate);

                ps.setInt(1, 1);                                   // user_id
                ps.setDouble(2, cycle.getTotalAllowance());
                ps.setString(3, cycle.getStartDate().toString());
                ps.setString(4, cycle.getEndDate().toString());
                ps.setDouble(5, cycle.getRemainingBalance());
                ps.setDouble(6, cycle.getSafeDailyLimit());
                ps.executeUpdate();

                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) cycle.setCycleId(keys.getInt(1));

                conn.commit();
                System.out.println("[DB] Cycle saved, ID=" + cycle.getCycleId());

            } catch (SQLException inner) {
                conn.rollback();
                System.err.println("[DB] saveCycle rolled back: " + inner.getMessage());
                throw inner;
            }
        } catch (SQLException e) {
            System.err.println("[DB] saveCycle error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param cycle the cycle object with updated values
     */
    public void updateCycle(BudgetCycle cycle) {
  String sql = """
                UPDATE budget_cycles
                SET remaining_balance = ?, safe_daily_limit = ?, active = ?
                WHERE cycle_id = ?
                """;
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, cycle.getRemainingBalance());
            ps.setDouble(2, cycle.getSafeDailyLimit());
            ps.setInt(3, cycle.isActive() ? 1 : 0);
            ps.setInt(4, cycle.getCycleId());
            ps.executeUpdate();
            System.out.println("[DB] Cycle updated ID=" + cycle.getCycleId()
                    + " balance=" + cycle.getRemainingBalance());
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Returns the active cycle for the given user, or {@code null} if none exists (US#3).
     *
     * @param userId the user's ID (always 1 in Masroofy)
     * @return the active {@link BudgetCycle}, or {@code null}
     */
    public BudgetCycle getActiveCycle(int userId) {
      String sql = "SELECT * FROM budget_cycles WHERE user_id = ? AND active = 1 ORDER BY cycle_id DESC LIMIT 1";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                BudgetCycle c = new BudgetCycle();
                c.setCycleId(rs.getInt("cycle_id"));
                c.setTotalAllowance(rs.getDouble("total_allowance"));
                c.setStartDate(LocalDate.parse(rs.getString("start_date")));
                c.setEndDate(LocalDate.parse(rs.getString("end_date")));
                c.setRemainingBalance(rs.getDouble("remaining_balance"));
                c.setSafeDailyLimit(rs.getDouble("safe_daily_limit"));
                c.setActive(rs.getInt("active") == 1);
                System.out.println("[DB] Active cycle found ID=" + c.getCycleId());
                return c;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        System.out.println("[DB] No active cycle for user " + userId);
        return null;
    }

    /**
     * Deactivates a cycle and deletes all its transactions (used for reset / new cycle).
     *
     * @param cycleId the ID of the cycle to clear
     */
    public void clearCycleData(int cycleId) {
        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM transactions WHERE cycle_id = " + cycleId);
            stmt.execute("UPDATE budget_cycles SET active = 0 WHERE cycle_id = " + cycleId);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // ── Transactions ──────────────────────────────────────────────

    /**
     * Inserts a new transaction and sets the auto-generated ID on the object (US#2).
     *
     * @param cycleId the active cycle's ID
     * @param tx      the transaction to persist (transactionId will be set after save)
     */
    public void saveTransaction(int cycleId, Transaction tx) {
      String sql = """
                INSERT INTO transactions (cycle_id, amount, note, category_type, timestamp)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, cycleId);
            ps.setDouble(2, tx.getAmount());
            ps.setString(3, tx.getNote());
            ps.setString(4, tx.getCategory().getType().name());
            ps.setString(5, DateUtils.toStorageDateTimeString(tx.getTimestamp()));
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) tx.setTransactionId(keys.getInt(1));
            System.out.println("[DB] Transaction saved ID=" + tx.getTransactionId());
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Returns all transactions for a cycle, newest first (US#7).
     *
     * @param cycleId the cycle to query
     * @return list of transactions, may be empty
     */
    public List<Transaction> getTransactions(int cycleId) {
        String sql = "SELECT * FROM transactions WHERE cycle_id = ? ORDER BY timestamp DESC";
        List<Transaction> list = new ArrayList<>();
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cycleId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CategoryType  ct  = CategoryType.valueOf(rs.getString("category_type"));
                Category      cat = new Category(ct);
                LocalDateTime ts  = DateUtils.fromStorageDateTimeString(rs.getString("timestamp"));
                list.add(new Transaction(rs.getInt("transaction_id"),
                        rs.getDouble("amount"), rs.getString("note"), cat, ts));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /**
     * Updates a transaction's amount, category, and note in place (US#7 edit).
     *
     * @param tx the transaction with updated fields; transactionId identifies the row
     */
    public void updateTransaction(Transaction tx) {
        String sql = "UPDATE transactions SET amount=?, category_type=?, note=? WHERE transaction_id=?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, tx.getAmount());
            ps.setString(2, tx.getCategory().getType().name());
            ps.setString(3, tx.getNote());
            ps.setInt(4, tx.getTransactionId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Permanently deletes a transaction (US#7 delete).
     *
     * @param transactionId the ID of the transaction to delete
     */
    public void deleteTransaction(int transactionId) {
       try (Connection conn = DatabaseHelper.connect();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM transactions WHERE transaction_id = ?")) {
            ps.setInt(1, transactionId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Filtered transaction query — supports category, date-from, and date-to filters (US#7).
     *
     * @param cycleId  the active cycle ID
     * @param type     filter by category (null = all categories)
     * @param fromDate ISO date string "yyyy-MM-dd" (null = no lower bound)
     * @param toDate   ISO date string "yyyy-MM-dd" (null = no upper bound)
     * @return filtered and sorted list of transactions
     */
    public List<Transaction> filterTransactions(int cycleId, CategoryType type,
                                                String fromDate, String toDate) {
  StringBuilder sql = new StringBuilder(
                "SELECT * FROM transactions WHERE cycle_id = ?");
        if (type     != null) sql.append(" AND category_type = ?");
        if (fromDate != null) sql.append(" AND timestamp >= ?");
        if (toDate   != null) sql.append(" AND timestamp <= ?");
        sql.append(" ORDER BY timestamp DESC");

        List<Transaction> list = new ArrayList<>();
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int i = 1;
            ps.setInt(i++, cycleId);
            if (type     != null) ps.setString(i++, type.name());
            if (fromDate != null) ps.setString(i++, fromDate);
            if (toDate   != null) ps.setString(i++, toDate + " 23:59:59");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CategoryType  ct  = CategoryType.valueOf(rs.getString("category_type"));
                Category      cat = new Category(ct);
                LocalDateTime ts  = DateUtils.fromStorageDateTimeString(rs.getString("timestamp"));
                list.add(new Transaction(rs.getInt("transaction_id"),
                        rs.getDouble("amount"), rs.getString("note"), cat, ts));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}
