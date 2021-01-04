package org.francescoborri.chat.client.ui;

import org.francescoborri.chat.UsernameAlreadyTakenException;
import org.francescoborri.chat.client.App;
import org.francescoborri.chat.client.Client;

import org.apache.commons.lang3.StringUtils;

import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {
    public TextField ip;
    public TextField port;
    public TextField username;
    public PasswordField password;

    public void login() {
        try {
            if (port.getText().isBlank())
                port.setText(String.valueOf(Client.DEFAULT_PORT));

            if (!ip.getText().isBlank() && StringUtils.isNumeric(port.getText()) && !username.getText().isBlank())
                App.login(ip.getText(), Integer.parseInt(port.getText()), username.getText());
            else
                alert("Fill in all the required fields.");
        } catch (UsernameAlreadyTakenException exception) {
            alert("The username is already taken.");
        } catch (IOException ignored) {
            alert("Something went wrong...");
        }
    }

    private void alert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(content);
        alert.showAndWait();
    }
}
