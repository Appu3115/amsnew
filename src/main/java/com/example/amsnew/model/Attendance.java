package com.example.amsnew.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="attendance")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Attendance {
      
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotNull
	private String employeeId;
	private LocalDate attendanceDate;
	private LocalDateTime login;
	private LocalDateTime logout;
	private long overtime;
	
	@Enumerated(EnumType.STRING)
	private AttendanceStatus status;
	
	@ManyToOne
	@JoinColumn(name = "employee_ref_id")
	private Employees employee;


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public LocalDate getAttendanceDate() {
		return attendanceDate;
	}
	public void setAttendanceDate(LocalDate attendanceDate) {
		this.attendanceDate = attendanceDate;
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
	public AttendanceStatus getStatus() {
		return status;
	}
	public void setStatus(AttendanceStatus status) {
		this.status = status;
	}

	public Employees getEmployee() {
		return employee;
	}
	public void setEmployee(Employees employee) {
		this.employee = employee;
	}
	public long getOvertime() {
		return overtime;
	}
	public void setOvertime(long overtime) {
		this.overtime = overtime;
	}
	
	
	
	
	
}

