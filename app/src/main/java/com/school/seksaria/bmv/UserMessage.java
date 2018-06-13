package com.school.seksaria.bmv;

public class UserMessage {
    private String userName;
    private String messageContext;

    public UserMessage() {}

    public UserMessage(String tUserName, String tMessageContext) {
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
