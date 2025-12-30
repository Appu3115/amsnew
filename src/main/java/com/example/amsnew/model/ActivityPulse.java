package com.example.amsnew.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "activity_pulse")
public class ActivityPulse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String employeeId;
    private LocalDateTime pulseTime;

    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

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

	public LocalDateTime getPulseTime() {
		return pulseTime;
	}

	public void setPulseTime(LocalDateTime pulseTime) {
		this.pulseTime = pulseTime;
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

    
}
