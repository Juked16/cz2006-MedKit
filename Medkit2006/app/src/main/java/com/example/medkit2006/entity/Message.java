package com.example.medkit2006.entity;

import java.time.Instant;
import java.util.Date;

public class Message {

    private final String sender;
    private String content;
    private final Date timestamp;


    public Message(String sender, String content) {
        this(sender, content, Date.from(Instant.now()));
    }

    public Message(String sender, String content, Date timestamp) {
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
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

    public void setContent(String content) {
        this.content = content;
    }
}
