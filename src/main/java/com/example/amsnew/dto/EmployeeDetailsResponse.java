package com.example.amsnew.dto;

public class EmployeeDetailsResponse {

	    private String employeeId;
	    private String firstName;
	    private String lastName;
	    private String email;
	    private String phone;
	    private String role;
	    private boolean active;

	    private DepartmentDTO department;
	    private ShiftDTO shift;
	    
	    
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
		public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}
		public boolean isActive() {
			return active;
		}
		public void setActive(boolean active) {
			this.active = active;
		}
		public DepartmentDTO getDepartment() {
			return department;
		}
		public void setDepartment(DepartmentDTO department) {
			this.department = department;
		}
		public ShiftDTO getShift() {
			return shift;
		}
		public void setShift(ShiftDTO shift) {
			this.shift = shift;
		}
	    
	    
	    

}
