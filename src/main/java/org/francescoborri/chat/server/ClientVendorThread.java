package org.francescoborri.chat.server;

import org.francescoborri.chat.*;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Objects;

public class ClientVendorThread extends Thread {
    private final Socket socket;
    private final UserInformation userInformation;
    private final String clientUsername;
    private final BufferedReader in;
    private final PrintWriter out;

    public ClientVendorThread(Socket socket, UserInformation userInformation, String clientUsername) throws IOException, IllegalAccessException {
        super(String.format("%s:%d-vendor", socket.getInetAddress().getCanonicalHostName(), socket.getPort()));
        this.socket = socket;
        this.userInformation = userInformation;
        this.clientUsername = clientUsername;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        Server.getInstance().broadcast(
                userInformation.getInetSocketAddress(),
                new ChatMessage(
                        "server",
                        String.format("%s joined the chat", clientUsername),
                        LocalDateTime.now()
                ).toJSON()
        );
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public InetSocketAddress getClientSocketAddress() {
        return (InetSocketAddress) socket.getRemoteSocketAddress();
    }

    public void run() {
        try {
            boolean disconnect;
            do disconnect = manage(in.readLine());
            while (!disconnect);
        } catch (IOException | IllegalAccessException ignored) {
        }

        try {
            close();
        } catch (IOException | IllegalAccessException ignored) {
        }
    }

    public void send(JSONObject json) {
        out.println(json.toString());
    }

    public boolean manage(String request) throws IllegalAccessException {
        boolean disconnect;

        if (request == null)
            return true;

        Message message = MessageFactory.getMessage(new JSONObject(request));

        switch (message.getMessageType()) {
            case CHAT_MESSAGE:
                Server.getInstance().broadcast(userInformation.getInetSocketAddress(), message.toJSON());
                userInformation.newMessage();
                disconnect = false;
                break;
            case DISCONNECTION_MESSAGE:
                disconnect = true;
                break;
            case LOGIN_MESSAGE:
            default:
                throw new IllegalStateException();
        }

        return disconnect;
    }

    public void close() throws IOException, IllegalAccessException {
        Server.getInstance().connectionOnClose(this);
        socket.shutdownOutput();
        socket.close();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientVendorThread)) return false;
        ClientVendorThread that = (ClientVendorThread) o;
        return Objects.equals(socket, that.socket) && Objects.equals(userInformation, that.userInformation);
    }
}
