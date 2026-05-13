package com.example.llmchat;

public class Message {

    private int id;
    private String email;
    private String message;
    private String speaker;
    private String timestamp;

    // Constructor
    public Message(int id, String email, String message, String speaker, String timestamp){
        this.id = id;
        this.email = email;
        this.message = message;
        this.speaker = speaker;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getId(){
        return id;
    }

    public String getEmail(){
        return email;
    }

    public String getMessage(){
        return message;
    }

    public String getSpeaker(){
        return speaker;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setSpeaker(String speaker){
        this.speaker = speaker;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
