package com.example.amsnew.dto;

import java.util.List;

import com.example.amsnew.model.Employees;

public class DepartmentListResponse {

    private Long id;
    private String deptName;
    private String departmentCode;
    private boolean active;
    private int employeeCount;
    private List<Employees> employees;
    
    
    
	public List<Employees> getEmployees() {
		return employees;
	}
	public void setEmployees(List<Employees> employees) {
		this.employees = employees;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long long1) {
		this.id = long1;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public int getEmployeeCount() {
		return employeeCount;
	}
	public void setEmployeeCount(int employeeCount) {
		this.employeeCount = employeeCount;
	}

   
}

