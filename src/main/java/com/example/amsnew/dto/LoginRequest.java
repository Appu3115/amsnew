package com.example.amsnew.dto;

public class LoginRequest {

    private String employeeId;
    private String email;
    private String password;
    private Integer shiftId;
    private boolean workFromHome;
    
	public boolean isWorkFromHome() {
		return workFromHome;
	}
	public void setWorkFromHome(boolean workFromHome) {
		this.workFromHome = workFromHome;
	}
	public Integer getShiftId() {
		return shiftId;
	}
	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}