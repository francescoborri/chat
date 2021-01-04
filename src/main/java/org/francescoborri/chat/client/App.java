package org.francescoborri.chat.client;

import org.francescoborri.chat.UsernameAlreadyTakenException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private static Stage stage = null;
    private static Client client = null;

    @Override
    public void start(Stage stage) throws IOException {
        App.stage = stage;
        load("login.fxml");
    }

    public static void main(String[] args) {
        launch();
    }

    private static void load(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml));
        Scene scene = new Scene(loader.load());
        stage.setUserData(loader.getController());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    public static void login(String ip, int port, String username) throws IOException, UsernameAlreadyTakenException {
        client = new Client(ip, port, username);

        if (!client.connect()) {
            client = null;
            throw new UsernameAlreadyTakenException();
        } else {
            load("main.fxml");
            client.start();
        }
    }

    public static Stage getStage() {
        return stage;
    }

    public static Client getClient() {
        return client;
    }
}