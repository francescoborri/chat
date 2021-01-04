package org.francescoborri.chat;

import java.net.InetSocketAddress;

public class User {
    private final InetSocketAddress socketAddress;
    private final String name;
    private int messages;

    public User(InetSocketAddress socketAddress, String name) {
        this.socketAddress = socketAddress;
        this.name = name;
        messages = 0;
    }

    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

    public String getName() {
        return name;
    }

    public int getMessages() {
        return messages;
    }

    public void newRequest() {
        messages++;
    }
}
