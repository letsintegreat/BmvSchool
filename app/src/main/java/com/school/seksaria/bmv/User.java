package com.school.seksaria.bmv;

public class User {

    private String what;
    private boolean validity = false;
    private String fullName;
    private int classNumber;
    private String token;

    public User() {}

    public User(String cwhat, boolean cvalidity, String cfullName, int cclassNumber, String ctoken) {
        what = cwhat;
        this.classNumber = cclassNumber;
        this.fullName = cfullName;
        this.validity = cvalidity;
        this.token = ctoken;
    }

    public String getWhat() { return what; }

    public boolean getValidity() { return validity; }

    public String getFullName() { return fullName; }

    public int getClassNumber() { return classNumber; }

    public String getToken() { return token; }

    public void setWhat(String what) { this.what = what; }

    public void setValidity(boolean validity) { this.validity = validity; }

    public void setFullName(String fullName) { this.fullName = fullName; }

    public void setClassNumber(int classNumber) { this.classNumber = classNumber; }

    public void setToken(String token) { this.token = token; }
}
