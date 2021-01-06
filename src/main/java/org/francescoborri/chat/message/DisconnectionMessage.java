package org.francescoborri.chat.message;

import org.json.JSONObject;

public class DisconnectionMessage implements Message {
    private static final MessageType messageType = MessageType.DISCONNECTION_MESSAGE;

    @Override
    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public JSONObject toJSON() {
        return new JSONObject().put("code", messageType.getCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o instanceof LoginMessage;
    }
}
