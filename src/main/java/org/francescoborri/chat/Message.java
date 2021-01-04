package org.francescoborri.chat;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private final int code;
    private final String data;
    private final User user;
    private final LocalDateTime dateTime;

    public Message(int code, String data, User user, LocalDateTime dateTime) {
        this.code = code;
        this.data = data;
        this.user = user;
        this.dateTime = dateTime;
    }

    public static Message loadFromJSON(User user, JSONObject json) {
        return new Message(
                json.getInt("code"),
                json.getString("data"),
                user,
                LocalDateTime.parse(json.getString("dateTime"), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
    }

    public int getCode() {
        return code;
    }

    public String getData() {
        return data;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
