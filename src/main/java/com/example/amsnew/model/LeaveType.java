package com.example.amsnew.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LeaveType {
    SICK,
    CASUAL,
    EARNED;

    @JsonCreator
    public static LeaveType fromString(String value) {
        for (LeaveType type : LeaveType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid LeaveType: " + value);
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
