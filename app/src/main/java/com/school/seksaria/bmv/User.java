package com.school.seksaria.bmv;

public class User {

    private String what;
    private int validity = 0;
    private String fullName;
    private int classNumber;
    private String validatedBy = "";
    private String rejectedMessage = "";

    public User() {}

    public User(String cwhat, int cvalidity, String cfullName, int cclassNumber) {
        this.what = cwhat;
        this.classNumber = cclassNumber;
        this.fullName = cfullName;
        this.validity = cvalidity;
    }

    public User(String cwhat, String cfullName, int cclassNumber) {
        this.what = cwhat;
        this.classNumber = cclassNumber;
        this.fullName = cfullName;
    }

    public String getWhat() { return what; }
    public int getValidity() { return validity; }
    public String getFullName() { return fullName; }
    public int getClassNumber() { return classNumber; }
    public String getValidatedBy() { return validatedBy; }
    public String getRejectedMessage() { return rejectedMessage; }
    public void setWhat(String what) { this.what = what; }
    public void setValidity(int validity) { this.validity = validity; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setClassNumber(int classNumber) { this.classNumber = classNumber; }
    public void setValidatedBy(String validatedBy) { this.validatedBy = validatedBy; }
    public void setRejectedMessage(String rejectedMessage) { this.rejectedMessage = rejectedMessage; }
}
