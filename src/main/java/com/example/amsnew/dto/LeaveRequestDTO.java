package com.example.amsnew.dto;

import java.time.LocalDate;
import java.util.List;

import com.example.amsnew.model.LeaveReason;
//import com.example.amsnew.model.LeaveStatus;
import com.example.amsnew.model.LeaveType;

import jakarta.validation.constraints.NotNull;

public class LeaveRequestDTO {

    @NotNull
    private LeaveType leaveType;

    @NotNull
    private LeaveReason reason;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
    
    @NotNull
    private List<String> proofUrls;

    // getters & setters
    public LeaveType getLeaveType() {
        return leaveType;
    }

    public List<String> getProofUrls() {
		return proofUrls;
	}

	public void setProofUrls(List<String> proofUrls) {
		this.proofUrls = proofUrls;
	}

	public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public LeaveReason getReason() {
        return reason;
    }

    public void setReason(LeaveReason reason) {
        this.reason = reason;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}