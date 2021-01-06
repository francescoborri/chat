package org.francescoborri.chat;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ChatMessage implements Message {
    private static final MessageType messageType = MessageType.CHAT_MESSAGE;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final String username;
    private final String data;
    private final LocalDateTime dateTime;

    public ChatMessage(JSONObject json) {
        this(json.getString("username"), json.getString("data"), LocalDateTime.parse(json.getString("dateTime"), formatter));
    }

    public ChatMessage(String username, String data, LocalDateTime dateTime) {
        this.username = username;
        this.data = data;
        this.dateTime = dateTime;
    }

    @Override
    public MessageType getMessageType() {
        return messageType;
    }

    public String getUsername() {
        return username;
    }

    public String getData() {
        return data;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public JSONObject toJSON() {
        return new JSONObject().put("code", messageType.getCode()).put("username", username).put("data", data).put("dateTime", formatter.format(dateTime));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatMessage)) return false;
        ChatMessage that = (ChatMessage) o;
        return Objects.equals(username, that.username) && Objects.equals(data, that.data) && Objects.equals(dateTime, that.dateTime);
    }
}
