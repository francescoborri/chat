package org.francescoborri.chat.server;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Server server;

        System.out.printf("Server port (default %d): ", Server.DEFAULT_PORT);
        String temp = scanner.nextLine();

        System.out.print("Server password: ");
        String password = scanner.nextLine();

        try {
            if (temp.isBlank() || !StringUtils.isNumeric(temp))
                server = Server.init(password);
            else
                server = Server.init(Integer.parseInt(temp), password);
            server.start();
        } catch (IOException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }
}
