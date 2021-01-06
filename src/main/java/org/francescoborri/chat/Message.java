package org.francescoborri.chat;

import org.json.JSONObject;

public interface Message {
    MessageType getMessageType();
    JSONObject toJSON();
}
