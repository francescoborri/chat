package org.francescoborri.chat.server;

import org.francescoborri.chat.Message;
import org.francescoborri.chat.MessageFactory;
import org.francescoborri.chat.LoginMessage;
import org.json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Server extends Thread {
    private static Server instance = null;

    private final ServerSocket server;
    private final HashMap<InetSocketAddress, UserInformation> users;
    private final HashSet<String> usernames;
    private final ArrayList<ClientVendorThread> aliveConnections;
    private final String password;

    public synchronized static Server getInstance() throws IllegalAccessException {
        if (instance == null)
            throw new IllegalAccessException();
        return instance;
    }

    public static synchronized Server init(int port, String password) throws IllegalAccessException, IOException {
        if (instance != null)
            throw new IllegalAccessException();
        instance = new Server(port, password);
        return instance;
    }

    private Server(int port, String password) throws IOException {
        super("chat-server");

        server = new ServerSocket(port);
        users = new HashMap<>();
        usernames = new HashSet<>();
        aliveConnections = new ArrayList<>();
        this.password = password;
    }

    @Override
    public synchronized void start() {
        CommandLine.getInstance().start();
        super.start();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Socket socket = server.accept();
                String clientUsername = login(socket);

                if (clientUsername == null)
                    continue;

                InetSocketAddress address = (InetSocketAddress) socket.getRemoteSocketAddress();
                ClientVendorThread clientVendorThread = new ClientVendorThread(socket, users.get(address), clientUsername);
                clientVendorThread.start();

                aliveConnections.add(clientVendorThread);
            } catch (IOException | IllegalAccessException ignored) {
            }
        }
    }

    public HashMap<InetSocketAddress, UserInformation> getUsers() {
        return users;
    }

    public ArrayList<ClientVendorThread> getAliveConnections() {
        return aliveConnections;
    }

    public void addUser(UserInformation userInformation) {
        users.put(userInformation.getInetSocketAddress(), userInformation);
    }

    private String login(Socket socket) throws IOException, IllegalAccessException {
        InetSocketAddress inetSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        Message reply;
        String clientUsername;
        Message message = MessageFactory.getMessage(new JSONObject(in.readLine()));

        switch (message.getMessageType()) {
            case LOGIN_MESSAGE:
                clientUsername = ((LoginMessage) message).getData();
                break;
            case CHAT_MESSAGE:
            case DISCONNECTION_MESSAGE:
            default:
                throw new IllegalStateException();
        }

        if (users.get(inetSocketAddress) != null) {
            reply = LoginMessage.accept();
        } else if (Server.getInstance().usernameUnavailable(clientUsername)) {
            reply = LoginMessage.deny();
            clientUsername = null;
        } else {
            reply = LoginMessage.accept();
            Server.getInstance().addUser(new UserInformation(inetSocketAddress));
        }

        usernames.add(clientUsername);
        out.println(reply.toJSON().toString());
        return clientUsername;
    }

    public boolean usernameUnavailable(String username) {
        return usernames.contains(username);
    }

    public void broadcast(InetSocketAddress source, JSONObject message) {
        for (ClientVendorThread clientVendorThread : aliveConnections) {
            if (clientVendorThread.getClientSocketAddress().equals(source))
                continue;

            clientVendorThread.send(message);
        }
    }

    public void connectionOnClose(ClientVendorThread clientVendorThread) {
        aliveConnections.remove(clientVendorThread);
        usernames.remove(clientVendorThread.getClientUsername());
    }

    public void close() throws IOException, IllegalAccessException {
        this.interrupt();

        while (!aliveConnections.isEmpty())
            aliveConnections.get(0).close();

        server.close();
        CommandLine.getInstance().close();
    }
}
