package org.francescoborri.chat.client;

import org.francescoborri.chat.client.ui.MainController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client extends Thread {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final String username;
    public final static int DEFAULT_PORT = 9090;

    public Client(String ip, int port, String username) throws IOException {
        super(String.format("client-%s", username));
        socket = new Socket();
        socket.connect(new InetSocketAddress(ip, port));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        this.username = username;
    }

    public boolean connect() throws IOException {
        send(username);
        return !receive().equals("ok");
    }

    public void run() {
        MainController ui = null;
        if (App.getStage().getUserData() != null && App.getStage().getUserData() instanceof MainController)
            ui = (MainController) App.getStage().getUserData();

        try {
            if (ui == null)
                throw new IOException();

            while (!currentThread().isInterrupted())
                ui.receive(receive());
        } catch (IOException ignored) {
        } finally {
            try {
                disconnect();
            } catch (IOException ignored) {
            }
        }
    }

    public void disconnect() throws IOException {
        this.interrupt();
        socket.shutdownOutput();
        socket.close();
    }

    public void send(String request) throws IOException {
        if (request.equals("exit") || request.equals("shutdown"))
            disconnect();
        else if (!request.isEmpty() && !request.equals("\n"))
            out.println(request);
    }

    public String receive() throws IOException {
        String received = in.readLine();
        if (received == null)
            throw new IOException();
        return received;
    }
}
