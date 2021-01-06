package org.francescoborri.chat.client;

import org.francescoborri.chat.exception.LoginException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private static Stage stage = null;
    private static Client client = null;
    public static Thread applicationThread = null;

    @Override
    public void start(Stage stage) throws IOException {
        App.stage = stage;
        App.applicationThread = Thread.currentThread();
        load("login.fxml");
    }

    public static void main(String[] args) {
        launch();
    }

    private static void load(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void login(String ip, int port, String username, String password) throws IOException, LoginException {
        client = new Client(ip, port, username);

        if (!client.connect(password)) {
            client = null;
            throw new LoginException();
        } else load("main.fxml");
    }

    public static Stage getStage() {
        return stage;
    }

    public static Client getClient() {
        return client;
    }
}