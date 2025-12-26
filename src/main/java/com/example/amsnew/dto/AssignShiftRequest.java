package com.example.amsnew.dto;

import java.time.LocalDate;
//import java.time.LocalDateTime;

public class AssignShiftRequest {

	private String employeeId;
    private Integer shiftId;
    private LocalDate startDate;
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public Integer getShiftId() {
		return shiftId;
	}
	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

}
