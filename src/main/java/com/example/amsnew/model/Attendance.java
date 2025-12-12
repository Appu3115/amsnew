package com.example.amsnew.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="attendance")
public class Attendance {
      
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private String employeeId;
	private String shiftId;
	private LocalDateTime login;
	private LocalDateTime logout;
	private String status;
	private LocalDateTime late;
	private LocalDateTime overtime;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getShiftId() {
		return shiftId;
	}
	public void setShiftId(String shiftId) {
		this.shiftId = shiftId;
	}
	public LocalDateTime getLogin() {
		return login;
	}
	public void setLogin(LocalDateTime login) {
		this.login = login;
	}
	public LocalDateTime getLogout() {
		return logout;
	}
	public void setLogout(LocalDateTime logout) {
		this.logout = logout;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDateTime getLate() {
		return late;
	}
	public void setLate(LocalDateTime late) {
		this.late = late;
	}
	public LocalDateTime getOvertime() {
		return overtime;
	}
	public void setOvertime(LocalDateTime overtime) {
		this.overtime = overtime;
	}
	
	
}

