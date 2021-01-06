package org.francescoborri.chat.client.ui;

import javafx.application.Platform;
import org.francescoborri.chat.ChatMessage;
import org.francescoborri.chat.client.App;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainController {
    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public TextField inputTextField;
    public Button sendButton;
    public ListView<String> messagesListView;

    @FXML
    public void initialize() {
        App.getStage().setUserData(this);
        App.getStage().setOnCloseRequest(windowEvent -> {
            try {
                App.getClient().close();
            } catch (IOException ignored) {
            }
        });
        App.getClient().start();
    }

    public void addItem(String message) {
        Platform.runLater(() -> {
            messagesListView.getItems().add(message);
            messagesListView.scrollTo(messagesListView.getItems().size() - 1);
        });

    }

    public void receive(ChatMessage message) {
        Platform.runLater(() -> addItem(messageOutputFormatter(message.getUsername(), message.getData(), message.getDateTime())));
    }

    public void send() {
        String message = inputTextField.getText();

        if (message.isBlank())
            return;

        ChatMessage chatMessage = App.getClient().send(message);

        Platform.runLater(() -> {
            receive(chatMessage);
            inputTextField.setText("");
        });
    }

    public String messageOutputFormatter(String username, String message, LocalDateTime dateTime) {
        String output;

        if (username.equals("server"))
            output = String.format("[%s]\n%s", dateTime.format(dateTimeFormatter), message);
        else
            output = String.format("[%s] - %s\n%s", dateTime.format(dateTimeFormatter), username, message);

        return output;
    }
}
