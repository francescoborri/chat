package org.francescoborri.chat.client;

import com.google.common.hash.Hashing;
import org.francescoborri.chat.*;
import org.francescoborri.chat.client.ui.MainController;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class Client extends Thread {
    public final static int DEFAULT_PORT = 9090;

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final String username;

    public Client(String ip, int port, String username) throws IOException {
        super(String.format("client-%s", username));
        socket = new Socket();
        socket.connect(new InetSocketAddress(ip, port));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public boolean connect(String password) throws IOException {
        send(new LoginMessage(String.format("%s@%s", username, Hashing.sha256().hashString(password, StandardCharsets.UTF_8))).toJSON());
        return new LoginMessage(new JSONObject(receive())).equals(LoginMessage.accept());
    }

    @Override
    public void run() {
        MainController ui;
        if (App.getStage().getUserData() != null)
            ui = (MainController) App.getStage().getUserData();
        else
            throw new IllegalStateException();

        try {
            while (!this.isInterrupted()) {
                Message message = MessageFactory.getMessage(new JSONObject(receive()));

                switch (message.getMessageType()) {
                    case CHAT_MESSAGE:
                        ui.receive((ChatMessage) message);
                        break;
                    case ONLINE_USERS_MESSAGE:
                        ui.updateOnlineUsers((OnlineUsersMessage) message);
                        break;
                    case DISCONNECTION_MESSAGE:
                        interrupt();
                        break;
                    case LOGIN_MESSAGE:
                    default:
                        throw new IllegalStateException();
                }
            }
        } catch (IOException ignored) {
        } finally {
            try {
                if (!socket.isClosed())
                    close();
            } catch (IOException ignored) {
            }
        }
    }

    public void close() throws IOException {
        this.interrupt();
        send(new DisconnectionMessage().toJSON());
        socket.shutdownOutput();
        socket.close();
    }

    public ChatMessage send(String message) {
        ChatMessage chatMessage = new ChatMessage(username, message, LocalDateTime.now());
        out.println(chatMessage.toJSON().toString());
        return chatMessage;
    }

    public void send(JSONObject json) {
        out.println(json.toString());
    }

    public String receive() throws IOException {
        String received = in.readLine();
        if (received == null)
            this.interrupt();
        return received;
    }
}
