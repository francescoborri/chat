package org.francescoborri.chat.server;

import java.net.InetSocketAddress;

public class UserInformation {
    private final InetSocketAddress inetSocketAddress;
    private int messages;

    public UserInformation(InetSocketAddress inetSocketAddress) {
        this.inetSocketAddress = inetSocketAddress;
        messages = 0;
    }

    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }

    public int getMessages() {
        return messages;
    }

    public void newMessage() {
        messages++;
    }
}
