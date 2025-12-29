package com.example.amsnew.dto;

public class DepartmentAttendanceDTO {

    private String departmentName;
    private long totalEmployees;
    private long presentCount;
    private long lateCount;
    private long absentCount;

    public DepartmentAttendanceDTO(String departmentName,long totalEmployees,long presentCount,long lateCount,long absentCount)
    {
        this.departmentName = departmentName;
        this.totalEmployees = totalEmployees;
        this.presentCount = presentCount;
        this.lateCount = lateCount;
        this.absentCount = absentCount;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public long getTotalEmployees() {
        return totalEmployees;
    }

    public long getPresentCount() {
        return presentCount;
    }

    public long getLateCount() {
        return lateCount;
    }

    public long getAbsentCount() {
        return absentCount;
    }
}
