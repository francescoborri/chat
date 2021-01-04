package org.francescoborri.chat.server;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Server port: ");
        int port = scanner.nextInt();

        try {
            Server server = new Server("chat-server", port);
            server.start();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
