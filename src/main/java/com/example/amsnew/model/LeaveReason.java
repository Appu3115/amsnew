package com.example.amsnew.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LeaveReason {
    // For SICK
    HEADACHE,
    STOMACHACHE,
    FEVER,
    // For CASUAL
    PERSONAL,
    FAMILY,
    TRAVEL,
    // For EARNED
    VACATION;

    @JsonCreator
    public static LeaveReason fromString(String value) {
        for (LeaveReason reason : LeaveReason.values()) {
            if (reason.name().equalsIgnoreCase(value)) {
                return reason;
            }
        }
        throw new IllegalArgumentException("Invalid LeaveReason: " + value);
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
