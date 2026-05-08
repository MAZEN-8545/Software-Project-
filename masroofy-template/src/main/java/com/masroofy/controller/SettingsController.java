package com.masroofy.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.masroofy.Main;
import com.masroofy.util.AlertUtils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controller for settings.fxml — handles US#12 (Privacy Lock).
 *
 * @version 1.0
 */
public class SettingsController implements Initializable {

    @FXML
    private CheckBox chkPrivacyLock;
    @FXML
    private VBox pinSetupBox;
    @FXML
    private PasswordField txtPin;
    @FXML
    private PasswordField txtConfirmPin;
    @FXML
    private Label lblPinStatus;
    @FXML
    private Label lblLockStatus;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load current privacy lock status
        boolean isEnabled = Main.getRepository().isPrivacyLockEnabled();
        chkPrivacyLock.setSelected(isEnabled);
        updateUIState(isEnabled);
    }

    /**
     * Updates UI based on privacy lock state.
     */
    private void updateUIState(boolean enabled) {
        pinSetupBox.setVisible(!enabled); // Show PIN setup when enabling
        pinSetupBox.setManaged(!enabled);

        if (enabled) {
            lblLockStatus.setText("Privacy Lock: Enabled ✓");
            lblLockStatus.setStyle("-fx-font-size:12px;-fx-text-fill:#2ECC71;");
        } else {
            lblLockStatus.setText("Privacy Lock: Disabled");
            lblLockStatus.setStyle("-fx-font-size:12px;-fx-text-fill:#4E7A8E;");
        }
        lblPinStatus.setText("");
    }

    /**
     * US#12 — Handle privacy lock toggle.
     */
    @FXML
    private void onPrivacyLockToggle() {
        boolean isEnabled = chkPrivacyLock.isSelected();

        if (!isEnabled) {
            // Disabling the lock - require PIN verification first
            String pin = showPinInputDialog("Enter current PIN to disable lock:");
            if (pin == null) {
                chkPrivacyLock.setSelected(true); // Cancelled - keep enabled
                return;
            }

            if (!Main.getRepository().validatePin(pin)) {
                AlertUtils.showError("Incorrect PIN. Privacy lock remains enabled.");
                chkPrivacyLock.setSelected(true);
                return;
            }

            // Disable the lock
            Main.getRepository().disablePrivacyLock();
            AlertUtils.showInfo("Privacy Lock Disabled", "Your privacy lock has been turned off.");
        }

        updateUIState(isEnabled);
    }

    /**
     * US#12 — Save PIN when enabling privacy lock.
     */
    @FXML
    private void onSavePinClick() {
        String pin = txtPin.getText().trim();
        String confirmPin = txtConfirmPin.getText().trim();

        // Validate PIN
        if (pin.isEmpty()) {
            lblPinStatus.setText("Please enter a PIN.");
            return;
        }

        if (!pin.matches("\\d{4,6}")) {
            lblPinStatus.setText("PIN must be 4-6 digits.");
            return;
        }

        if (!pin.equals(confirmPin)) {
            lblPinStatus.setText("PINs do not match.");
            return;
        }

        // Enable privacy lock with the PIN
        Main.getRepository().enablePrivacyLock(pin);

        // Clear fields
        txtPin.clear();
        txtConfirmPin.clear();

        AlertUtils.showInfo("Privacy Lock Enabled", "Your privacy lock is now active. You will be prompted for your PIN on next app launch.");

        updateUIState(true);
    }

    /**
     * Shows a dialog to input PIN.
     */
    private String showPinInputDialog(String message) {
        javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog();
        dialog.setTitle("Privacy Lock");
        dialog.setHeaderText("Authentication Required");
        dialog.setContentText(message);

        // Make it a password field by customizing the dialog
        PasswordField pwdField = new PasswordField();
        pwdField.setPromptText("Enter PIN");

        dialog.getDialogPane().setContent(new javafx.scene.layout.VBox(10,
            new Label(message),
            pwdField
        ));

        return dialog.showAndWait().map(result -> {
            if (result.isEmpty()) {
                return pwdField.getText();
            }
            return result;
        }).orElse(null);
    }

    /**
     * Navigate back to dashboard.
     */
    @FXML
    private void onBackClick() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 1050, 720);
            scene.getStylesheets().add(
                    getClass().getResource("/css/styles.css").toExternalForm());
            Stage stage = (Stage) chkPrivacyLock.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Masroofy — Dashboard");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
