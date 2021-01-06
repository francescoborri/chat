package org.francescoborri.chat.client;

import org.francescoborri.chat.Message;
import org.francescoborri.chat.MessageType;
import org.json.JSONObject;

public class OnlineUsersMessage implements Message {
    private static final MessageType messageType = MessageType.ONLINE_USERS_MESSAGE;

    private final int onlineUsers;

    public OnlineUsersMessage(JSONObject json) {
        this(json.getInt("onlineUsers"));
    }

    public OnlineUsersMessage(int onlineUsers) {
        this.onlineUsers = onlineUsers;
    }

    @Override
    public MessageType getMessageType() {
        return messageType;
    }

    public int getOnlineUsers() {
        return onlineUsers;
    }

    @Override
    public JSONObject toJSON() {
        return new JSONObject().put("code", messageType.getCode()).put("onlineUsers", onlineUsers);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OnlineUsersMessage)) return false;
        OnlineUsersMessage that = (OnlineUsersMessage) o;
        return onlineUsers == that.onlineUsers;
    }
}
