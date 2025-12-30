package com.example.amsnew.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.example.amsnew.model.LeaveType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "leaverequest")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /* ===== EMPLOYEE ===== */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnoreProperties({"leaveRequests"})
    private Employees employee;

    /* ===== LEAVE DETAILS ===== */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveType leaveType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveReason reason;

    @Column(nullable = false)
    private LocalDate requestDate;

    @NotNull
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(nullable = false)
    private LocalDate endDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LeaveStatus status;

    private LocalDate approvedDate;

    

    



    /* ===== LEAVE PROOFS ===== */
    @OneToMany(
            mappedBy = "leaveRequest",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnoreProperties({"leaveRequest"})
    private List<LeaveProof> proofs = new ArrayList<>();


    /* ===== CONSTRUCTOR ===== */
    public LeaveRequest() {
    }

    /* ===== GETTERS & SETTERS ===== */
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

    public LeaveType getLeaveType() {
        return leaveType;
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

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
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

    public LeaveStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }

    public LocalDate getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(LocalDate approvedDate) {
        this.approvedDate = approvedDate;
    }

    public List<LeaveProof> getProofs() {
        return proofs;
    }

    public void setProofs(List<LeaveProof> proofs) {
        this.proofs = proofs;
    }

    /* ===== RELATION HELPERS ===== */
    public void addProof(LeaveProof proof) {
        proofs.add(proof);
        proof.setLeaveRequest(this);
    }

    public void removeProof(LeaveProof proof) {
        proofs.remove(proof);
        proof.setLeaveRequest(null);
    }
}
