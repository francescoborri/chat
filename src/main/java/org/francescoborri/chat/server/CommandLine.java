package org.francescoborri.chat.server;

import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class CommandLine extends Thread {
    private static CommandLine instance = null;
    private final Scanner scanner;

    public static CommandLine getInstance() {
        if (instance == null)
            instance = new CommandLine();
        return instance;
    }

    private CommandLine() {
        super("chat-server-cli");
        scanner = new Scanner(System.in);
    }

    public void run() {
        try {
            boolean end;

            do {
                System.out.printf("[%s] ", getPrefix());
                end = manage(scanner.nextLine());
            } while (!end && !Thread.interrupted());
        } catch (IOException | IllegalAccessException ignored) {
        }
    }

    public boolean manage(String command) throws IOException, IllegalAccessException {
        boolean end = false;
        System.out.print("\r");

        if (command.isBlank() || command.isEmpty())
            return false;

        switch (command) {
            case "shutdown":
                end = true;
                Server.getInstance().close();
                break;
            case "recap":
                recap();
                break;
            case "online":
                online();
                break;
            default:
                System.out.println("Unknown command");
                break;
        }

        return end;
    }

    public String getPrefix() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public void recap() throws IllegalAccessException {
        for (UserInformation userInformation : Server.getInstance().getUsers().values()) {
            System.out.printf("%s:%d sent %d request(s)\n",
                    userInformation.getInetSocketAddress().getAddress().getCanonicalHostName(),
                    userInformation.getInetSocketAddress().getPort(),
                    userInformation.getMessages()
            );
        }
    }

    public void online() throws IllegalAccessException {
        for (ClientVendorThread clientVendorThread: Server.getInstance().getAliveConnections()) {
            System.out.printf("%s is online\n", clientVendorThread.getClientUsername());
        }
    }

    public void close() {
        System.out.println("shutting down server...");
        this.interrupt();
    }
}
