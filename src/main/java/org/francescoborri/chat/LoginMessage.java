package org.francescoborri.chat;

import org.json.JSONObject;

import java.util.Objects;

public class LoginMessage implements Message {
    private static final MessageType messageType = MessageType.LOGIN_MESSAGE;
    private final String data;

    public LoginMessage(JSONObject json) {
        this(json.getString("data"));
    }

    public LoginMessage(String data) {
        this.data = data;
    }

    public static LoginMessage accept() {
        return new LoginMessage("true");
    }

    public static LoginMessage deny() {
        return new LoginMessage("false");
    }

    @Override
    public MessageType getMessageType() {
        return messageType;
    }

    public String getData() {
        return data;
    }

    @Override
    public JSONObject toJSON() {
        return new JSONObject().put("code", messageType.getCode()).put("data", data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginMessage)) return false;
        LoginMessage that = (LoginMessage) o;
        return Objects.equals(data, that.data);
    }
}
