package com.example.amsnew.model;

import java.time.LocalDate;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="leaverequest")
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employees employee;
    @NotNull
    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;
    @NotNull
    @Enumerated(EnumType.STRING)
    private LeaveReason reason;
    private LocalDate requestDate;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    private String status;
    private LocalDate approvedDate;
    

    public LeaveRequest() {}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employees getEmployee() {
        return employee;
    }

    public void setEmployee(Employees employee) {
        this.employee = employee;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(LocalDate approvedDate) {
        this.approvedDate = approvedDate;
    }

    @Override
    public String toString() {
        return "LeaveRequest{" +
                "id=" + id +
                ", employee=" + employee +
                ", leaveType='" + leaveType + '\'' +
                ", requestDate=" + requestDate +
                ", reason='" + reason + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                ", approvedDate=" + approvedDate +
                '}';
    }
}
