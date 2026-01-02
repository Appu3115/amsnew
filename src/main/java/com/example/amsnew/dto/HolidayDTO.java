package com.example.amsnew.dto;

import com.example.amsnew.model.HolidayType;

public class HolidayDTO {

    private String title;

    // 👇 receive as String
    private String date;   // "2026-01-01"

    private HolidayType type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public HolidayType getType() {
        return type;
    }

    public void setType(HolidayType type) {
        this.type = type;
    }
}
