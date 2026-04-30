package com.masroofy.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class providing date formatting and parsing helpers for Masroofy.
 *
 * <p>All methods are static — do not instantiate this class.</p>
 *
 * <p><b>OWNER: Mohamed Arafa Khalaf (20240517)</b></p>
 *
 * @version 1.0
 */
public class DateUtils {

    /** Formatter for SQLite DATE storage: {@code yyyy-MM-dd}. */
    public static final DateTimeFormatter DATE_FORMAT     = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    /** Formatter for SQLite DATETIME storage: {@code yyyy-MM-dd HH:mm:ss}. */
    public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /** Formatter for display in the UI: {@code dd MMM yyyy HH:mm}. */
    public static final DateTimeFormatter DISPLAY_FORMAT  = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");

    /** Private constructor — utility class, not meant to be instantiated. */
    private DateUtils() {}

    /**
     * Formats a {@link LocalDate} to ISO string for SQLite storage.
     *
     * @param date the date to format
     * @return string in format {@code yyyy-MM-dd}
     */
    public static String toStorageString(LocalDate date) { return date.format(DATE_FORMAT); }

    /**
     * Parses a SQLite ISO date string back to a {@link LocalDate}.
     *
     * @param dateStr string in format {@code yyyy-MM-dd}
     * @return parsed {@link LocalDate}
     */
    public static LocalDate fromStorageString(String dateStr) { return LocalDate.parse(dateStr, DATE_FORMAT); }

    /**
     * Formats a {@link LocalDateTime} to a human-readable string for UI display.
     *
     * @param dateTime the timestamp to format
     * @return string in format {@code dd MMM yyyy HH:mm}, e.g. {@code "29 Apr 2026 14:35"}
     */
    public static String toDisplayString(LocalDateTime dateTime) { return dateTime.format(DISPLAY_FORMAT); }

    /**
     * Formats a {@link LocalDateTime} to a SQLite storage string.
     *
     * @param dt the datetime to format
     * @return string in format {@code yyyy-MM-dd HH:mm:ss}
     */
    public static String toStorageDateTimeString(LocalDateTime dt) { return dt.format(DATETIME_FORMAT); }

    /**
     * Parses a SQLite datetime string back to {@link LocalDateTime}.
     *
     * @param s string in format {@code yyyy-MM-dd HH:mm:ss}
     * @return parsed {@link LocalDateTime}
     */
    public static LocalDateTime fromStorageDateTimeString(String s) { return LocalDateTime.parse(s, DATETIME_FORMAT); }
}
