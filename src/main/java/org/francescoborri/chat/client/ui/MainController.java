package org.francescoborri.chat.client.ui;

import org.francescoborri.chat.client.App;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;

public class MainController {
    public TextField input;
    public Button send;
    public ListView<String> messages;

    @FXML
    public void initialize() {
        App.getClient().start();
    }

    public void receive(String message) {
        messages.getItems().add(message);
    }

    public void send() throws IOException {
        App.getClient().send(input.getText());
    }
}
