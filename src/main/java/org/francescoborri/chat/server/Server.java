package org.francescoborri.chat.server;

import org.francescoborri.chat.User;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server extends Thread {
    private final ServerSocket server;
    private final HashMap<InetSocketAddress, User> users;
    private final HashSet<String> usernames;
    private final ThreadPoolExecutor aliveConnections;
    private final CommandLine commandLine;

    public Server(String name, int port) throws IOException {
        super(name);

        server = new ServerSocket(port);
        users = new HashMap<>();
        usernames = new HashSet<>();
        aliveConnections = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);
        commandLine = new CommandLine(this);

        commandLine.start();
    }

    public void run() {
        while (!Thread.interrupted()) {
            try {
                Socket socket = server.accept();
                String username = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();

                if (usernames.contains(username)) {

                }

                InetSocketAddress clientSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
                User user;

                if (users.containsKey(clientSocketAddress))
                    user = users.get(clientSocketAddress);
                else
                    user = new User(clientSocketAddress, username);

                ClientVendorThread clientVendorThread = new ClientVendorThread(username, this, socket, user);

                users.put(clientSocketAddress, user);
                aliveConnections.submit(clientVendorThread);
            } catch (IOException ignored) {
            }
        }
    }

    public HashMap<InetSocketAddress, User> getUsers() {
        return users;
    }

    public CommandLine getChatServerCommandLine() {
        return commandLine;
    }

    private void checkUsername(Socket socket) throws IOException {
        String username = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
        
    }

    public void broadcast(InetSocketAddress source, String message) {
        for (Map.Entry<InetSocketAddress, ClientVendorThread> entry : aliveConnections.) {
            if (entry.getKey().equals(source))
                continue;

            entry.getValue().send(message);
        }
    }

    public void connectionOnClose(InetSocketAddress key, boolean remove) {
        if (remove)
            connections.remove(key);
    }

    public void close() throws IOException {
        this.interrupt();

        for (ClientVendorThread connection : connections.values())
            connection.close(false);

        server.close();
        commandLine.close();
    }
}
