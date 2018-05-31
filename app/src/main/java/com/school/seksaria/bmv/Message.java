package com.school.seksaria.bmv;

public class Message {
    private String userName;
    private String messageContext;

    public Message() {}

    public Message(String tUserName, String tMessageContext) {
        userName = tUserName;
        messageContext = tMessageContext;
    }

    public String getMessageContext() {
        return messageContext;
    }

    public String getUserName() {
        return userName;
    }
}
