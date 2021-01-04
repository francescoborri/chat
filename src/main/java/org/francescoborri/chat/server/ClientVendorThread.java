package org.francescoborri.chat.server;

import org.francescoborri.chat.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientVendorThread extends Thread {
    private final Server server;
    private final User clientUser;
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public ClientVendorThread(String name, Server server, Socket socket, User clientUser) throws IOException {
        super(name);
        this.server = server;
        this.socket = socket;
        this.clientUser = clientUser;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        server.broadcast(
                clientUser.getSocketAddress(),
                (String.format("%s joined the chat", clientUser.getName()))
        );
    }

    public void run() {
        String last = null;

        try {
            last = receive();
        } catch (IOException ignored) {
        }

        try {
            close(true);
            if (last != null && last.equals("shutdown"))
                server.getChatServerCommandLine().manage(last);
        } catch (IOException ignored) {
        }
    }

    public void send(String message) {
        out.println(message);
    }

    public String receive() throws IOException {
        String request;

        do {
            request = in.readLine();
            clientUser.newRequest();
            manage(request);
        } while (request != null && !request.equals("exit") && !request.equals("shutdown"));

        return request;
    }

    public void manage(String request) {
        if (request == null)
            return;

        switch (request) {
            case "exit":
            case "shutdown":
                break;
            default:
                server.broadcast(
                        clientUser.getSocketAddress(),
                        String.format("[%s]: %s", getName(), request)
                );
                break;
        }
    }

    public void close(boolean remove) throws IOException {
        server.connectionOnClose(clientUser.getSocketAddress(), remove);
        socket.shutdownOutput();
        socket.close();
        server.broadcast(
                clientUser.getSocketAddress(),
                String.format("%s left the chat", clientUser.getName())
        );
    }
}
