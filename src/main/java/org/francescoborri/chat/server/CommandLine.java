package org.francescoborri.chat.server;

import org.francescoborri.chat.User;

import java.io.*;
import java.net.InetSocketAddress;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;

public class CommandLine extends Thread {
    private final Server server;
    private final Scanner scanner;

    public CommandLine(Server server) {
        super("chat-server-cli");
        this.server = server;
        scanner = new Scanner(System.in);
    }

    public void run() {
        try {
            String cmd;
            do {
                System.out.printf("[%s]: ", getPrefix());
                cmd = scanner.nextLine();
                manage(cmd);
            } while (!cmd.equals("shutdown") && !Thread.interrupted());
        } catch (IOException ignored) {
        }
    }

    public void manage(String cmd) throws IOException {
        System.out.print("\r");
        switch (cmd) {
            case "shutdown":
                server.close();
                break;
            case "recap":
                recap();
                break;
            case "online":
                online();
                break;
        }
    }

    public String getPrefix() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public void recap() {
        for (Map.Entry<InetSocketAddress, User> entry : server.getUsers().entrySet()) {
            System.out.printf("[%s | %s:%d] sent %d request(s)\n",
                    entry.getValue().getName(),
                    entry.getValue().getSocketAddress().getAddress().getCanonicalHostName(),
                    entry.getValue().getSocketAddress().getPort(),
                    entry.getValue().getMessages()
            );
        }
    }

    public void online() {
        for (Map.Entry<InetSocketAddress, ClientVendorThread> entry : server.getConnections().entrySet()) {
            System.out.printf("%s is online\n", entry.getValue().getName());
        }
    }

    public void close() throws IOException {
        System.out.println("shutting down server...");
        this.interrupt();
    }
}
