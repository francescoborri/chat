package org.francescoborri.chat.message;

import org.json.JSONObject;

public interface Message {
    MessageType getMessageType();
    JSONObject toJSON();
    boolean equals(Object o);
}
