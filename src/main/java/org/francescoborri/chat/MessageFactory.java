package org.francescoborri.chat;

import org.json.JSONObject;

public class MessageFactory {
    public static Message getMessage(JSONObject json) {
        Message message;

        switch (MessageType.fromCode(json.getInt("code"))) {
            case LOGIN_MESSAGE:
                message = new LoginMessage(json);
                break;
            case CHAT_MESSAGE:
                message = new ChatMessage(json);
                break;
            case DISCONNECTION_MESSAGE:
                message = new DisconnectionMessage();
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(json.getInt("code")));
        }

        return message;
    }
}
