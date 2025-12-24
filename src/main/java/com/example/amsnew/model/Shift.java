package com.example.amsnew.model;

import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="shift")
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    private String shiftType;
    @NotNull
    private LocalTime startTime;    
    @NotNull
    private LocalTime endTime;
    private int graceMinutes;
    
    private String shiftName;
    
    
    public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}

	public int getGraceMinutes() {
		return graceMinutes;
	}

	public void setGraceMinutes(int graceMinutes) {
		this.graceMinutes = graceMinutes;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
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

	public String getShiftName() {
		// TODO Auto-generated method stub
		return shiftName;
	}
}
