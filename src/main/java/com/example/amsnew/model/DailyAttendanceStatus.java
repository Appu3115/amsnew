package com.example.amsnew.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "daily_attendance_status",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"employee_id", "attendance_date"})
    }
)
public class DailyAttendanceStatus {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;

	    @ManyToOne
	    @JoinColumn(name = "employee_id", nullable = false)
	    private Employees employee;

	    @Column(name = "attendance_date", nullable = false)
	    private LocalDate attendanceDate;

	    @Enumerated(EnumType.STRING)
	    private DailyStatus status;
	    
	    private LocalDateTime loginTime;
	    private LocalDateTime logoutTime;
	    private Long permissionMinutes;

	    
	    
	    
		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public Employees getEmployee() {
			return employee;
		}

		public void setEmployee(Employees employee) {
			this.employee = employee;
		}

		public LocalDate getAttendanceDate() {
			return attendanceDate;
		}

		public void setAttendanceDate(LocalDate attendanceDate) {
			this.attendanceDate = attendanceDate;
		}

		public DailyStatus getStatus() {
			return status;
		}

		public void setStatus(DailyStatus status) {
			this.status = status;
		}

		public LocalDateTime getLoginTime() {
			return loginTime;
		}

		public void setLoginTime(LocalDateTime loginTime) {
			this.loginTime = loginTime;
		}

		public LocalDateTime getLogoutTime() {
			return logoutTime;
		}

		public void setLogoutTime(LocalDateTime logoutTime) {
			this.logoutTime = logoutTime;
		}

		public Long getPermissionMinutes() {
			return permissionMinutes;
		}

		public void setPermissionMinutes(Long permissionMinutes) {
			this.permissionMinutes = permissionMinutes;
		}

		
	    
	    
	    
	    
}
