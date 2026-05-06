package com.masroofy.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * Utility class for showing JavaFX alert dialogs.
 *
 * <b>OWNER: Mohamed Arafa Khalaf (20240517)</b>
 *
 * @version 1.0
 */
public class AlertUtils {

    private AlertUtils() {
    }

    /**
     * Shows an informational dialog and waits for the user to close it.
     *
     * @param title the dialog window title
     * @param message the message to display
     */
    public static void showInfo(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows a warning dialog (used for 80% and budget-exhausted budget alerts,
     * US#6).
     *
     * @param message the warning text to display
     */
    public static void showWarning(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Budget Warning");
        alert.setHeaderText("Budget Alert");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows an error dialog.
     *
     * @param message the error description to display
     */
    public static void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows a yes/no confirmation dialog.
     *
     * @param title the dialog window title
     * @param message the question to display
     * @return {@code true} if the user clicked OK; {@code false} if cancelled
     */
    public static boolean showConfirm(String title, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}
