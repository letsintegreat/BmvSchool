package com.school.seksaria.bmv;

public class Holiday {

    private String holidayDate;
    private String tag;

    public Holiday() {}

    public Holiday(String tDate, String tTag) {
        holidayDate = tDate;
        tag = tTag;
    }

    public String getHolidayDate() {
        return holidayDate;
    }

    public String getTag() {
        return tag;
    }
}
