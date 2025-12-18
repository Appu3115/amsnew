package com.example.amsnew.dto;

public class DepartmentAttendanceDTO {

    private String departmentName;
    private long presentCount;
    private long absentCount;

    public DepartmentAttendanceDTO(String departmentName,long presentCount,long absentCount) {
        this.departmentName = departmentName;
        this.presentCount = presentCount;
        this.absentCount = absentCount;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public long getPresentCount() {
        return presentCount;
    }

    public long getAbsentCount() {
        return absentCount;
    }
}
