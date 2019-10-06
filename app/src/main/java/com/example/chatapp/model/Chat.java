package com.example.chatapp.model;

public class Chat {

    private String sender;
    private String reciever;
    private String message;
    private boolean isSeen;

    public Chat() {}

    public Chat(String sender, String reciever, String message, boolean isSeen) {
        this.sender = sender;
        this.reciever = reciever;
        this.message = message;
        this.isSeen = isSeen;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }
}
