package org.francescoborri.chat.client.ui;

import org.francescoborri.chat.UsernameException;
import org.francescoborri.chat.client.App;
import org.francescoborri.chat.client.Client;

import org.apache.commons.lang3.StringUtils;

import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {
    public TextField ipAddressTextField;
    public TextField portTextField;
    public TextField usernameTextField;
    public PasswordField passwordField;

    public void login() {
        try {
            if (portTextField.getText().isBlank())
                portTextField.setText(String.valueOf(Client.DEFAULT_PORT));

            if (!ipAddressTextField.getText().isBlank() && StringUtils.isNumeric(portTextField.getText()))
                App.login(ipAddressTextField.getText(), Integer.parseInt(portTextField.getText()), usernameTextField.getText());
            else
                alert("Fill in all the required fields.");
        } catch (UsernameException exception) {
            alert("The username is already taken.");
        } catch (IOException ignored) {
            alert("Something went wrong...");
        }
    }

    private void alert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(content);
        alert.showAndWait();
    }
}
