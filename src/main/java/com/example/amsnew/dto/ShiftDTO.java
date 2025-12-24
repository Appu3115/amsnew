package com.example.amsnew.dto;

import java.time.LocalTime;

public class ShiftDTO {

	private int id;
    private String shiftName;
    private String shiftType;
    private LocalTime startTime;
    private LocalTime endTime;
    private int graceMinutes;
    
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getShiftName() {
		return shiftName;
	}
	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}
	public LocalTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	public LocalTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
	public int getGraceMinutes() {
		return graceMinutes;
	}
	public void setGraceMinutes(int graceMinutes) {
		this.graceMinutes = graceMinutes;
	}
	public String getShiftType() {
		return shiftType;
	}
	public void setShiftType(String shiftType) {
		this.shiftType = shiftType;
	}
    
    
    
    
    
    
    
    
	    
	    
	    
	    
	    

}
