package com.example.medkit2006.entity;

import java.util.Date;

public class Message {

    private String sender;
    private String reciever;
    private String content;
    private Date timestamp;

    public Message(String sender) {
        this.sender = sender;
        this.reciever = "";
        this.content = "";
        this.timestamp = new Date();
    }

    public Message(String sender, String receiver, String content) {
        this.sender = sender;
        this.reciever = receiver;
        this.content = content;
        this.timestamp = new Date();
    }


    public String getReciever() {
        return reciever;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
