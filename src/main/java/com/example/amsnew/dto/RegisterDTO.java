package com.example.amsnew.dto;

import jakarta.persistence.Transient;

public class RegisterDTO {

	 private String employeeId;
	    private String firstName;
	    private String lastName;
	    private String email;
	    private String password;
	    
	    @Transient
	    private String confirmPassword;
	    public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getConfirmPassword() {
			return confirmPassword;
		}
		public void setConfirmPassword(String confirmPassword) {
			this.confirmPassword = confirmPassword;
		}
		private String phone;
	    private int departmentId;
	    private String role;
	    private boolean isActive;

	    public String getEmployeeId() {
	        return employeeId;
	    }
	    public void setEmployeeId(String employeeId) {
	        this.employeeId = employeeId;
	    }
	    public String getFirstName() {
	        return firstName;
	    }
	    public void setFirstName(String firstName) {
	        this.firstName = firstName;
	    }
	    public String getLastName() {
	        return lastName;
	    }
	    public void setLastName(String lastName) {
	        this.lastName = lastName;
	    }
	    public String getEmail() {
	        return email;
	    }
	    public void setEmail(String email) {
	        this.email = email;
	    }
	    public String getPhone() {
	        return phone;
	    }
	    public void setPhone(String phone) {
	        this.phone = phone;
	    }
	    public int getDepartmentId() {
	        return departmentId;
	    }
	    public void setDepartmentId(int departmentId) {
	        this.departmentId = departmentId;
	    }
	    public String getRole() {
	        return role;
	    }
	    public void setRole(String role) {
	        this.role = role;
	    }
	    public boolean isActive() {
	        return isActive;
	    }
	    public void setActive(boolean active) {
	        isActive = active;
	    }
}
