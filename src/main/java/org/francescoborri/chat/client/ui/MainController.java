package org.francescoborri.chat.client.ui;


import org.francescoborri.chat.ChatMessage;
import org.francescoborri.chat.client.App;
import org.francescoborri.chat.client.OnlineUsersMessage;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Random;

public class MainController {
    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public TextField inputTextField;
    public Button sendButton;
    public ListView<TextFlow> messagesListView;
    public Label usernameLabel;
    public Label onlineUsersLabel;

    private final HashMap<String, Color> colors;

    public MainController() {
        colors = new HashMap<>();
    }

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

        Platform.runLater(() -> usernameLabel.setText(String.format("Your username: %s", App.getClient().getUsername())));
    }

    public void addItem(TextFlow message) {
        Platform.runLater(() -> {
            messagesListView.getItems().add(message);
            messagesListView.scrollTo(messagesListView.getItems().size() - 1);
        });
    }

    public void receive(ChatMessage message) {
        addItem(messageOutputFormatter(message.getUsername(), message.getData(), message.getDateTime()));
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

    public void updateOnlineUsers(OnlineUsersMessage onlineUsersMessage) {
        Platform.runLater(() -> onlineUsersLabel.setText(String.format("%d user online", onlineUsersMessage.getOnlineUsers())));
    }

    public TextFlow messageOutputFormatter(String username, String message, LocalDateTime dateTime) {
        TextFlow output = new TextFlow();

        Text messageText = new Text(message);
        Text dateTimeText = new Text(dateTime.format(dateTimeFormatter));
        dateTimeText.fontProperty().set(Font.font(9));

        if (username.equals("server")) {
            output.getChildren().addAll(messageText, new Text("\n"), dateTimeText);
        } else {
            Color usernameColor;
            Random random = new Random();

            if (colors.containsKey(username))
                usernameColor = colors.get(username);
            else {
                do {
                    usernameColor = Color.color(random.nextFloat(), random.nextFloat(), random.nextFloat());
                } while (colors.containsValue(usernameColor));
                colors.put(username, usernameColor);
            }

            Text usernameText = new Text(username);
            usernameText.setFill(usernameColor);

            output.getChildren().addAll(usernameText, new Text("\n"), messageText, new Text("\n"), dateTimeText);
        }

        return output;
    }
}
