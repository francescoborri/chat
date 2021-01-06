package org.francescoborri.chat;

public enum MessageType {
    LOGIN_MESSAGE(0),
    CHAT_MESSAGE(1),
    DISCONNECTION_MESSAGE(2);

    private final int code;

    MessageType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static MessageType fromCode(int code) {
        MessageType messageType;

        switch (code) {
            case 0:
                messageType = LOGIN_MESSAGE;
                break;
            case 1:
                messageType = CHAT_MESSAGE;
                break;
            case 2:
                messageType = DISCONNECTION_MESSAGE;
                break;
            default:
                throw new IllegalArgumentException();
        }

        return messageType;
    }
}
