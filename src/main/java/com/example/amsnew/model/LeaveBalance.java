package com.example.amsnew.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "leave_balance")
public class LeaveBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employees employee;

    private long casualLeave;
    private long sickLeave;
    private long earnedLeave;
    private long restrictedLeaveUsed; // max 2

    private int year;

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

	public long getCasualLeave() {
		return casualLeave;
	}

	public void setCasualLeave(long casualLeave) {
		this.casualLeave = casualLeave;
	}

	public long getSickLeave() {
		return sickLeave;
	}

	public void setSickLeave(long sickLeave) {
		this.sickLeave = sickLeave;
	}

	public long getEarnedLeave() {
		return earnedLeave;
	}

	public void setEarnedLeave(long earnedLeave) {
		this.earnedLeave = earnedLeave;
	}

	public long getRestrictedLeaveUsed() {
		return restrictedLeaveUsed;
	}

	public void setRestrictedLeaveUsed(long restrictedLeaveUsed) {
		this.restrictedLeaveUsed = restrictedLeaveUsed;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	
    
    
    
}
