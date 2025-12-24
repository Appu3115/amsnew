package com.example.amsnew.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "leave_proof")
public class LeaveProof {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Many files belong to one leave request
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_id", nullable = false)
    @JsonIgnore
    private LeaveRequest leaveRequest;

    // Original file name (for download)
    @Column(nullable = false)
    private String fileName;

    // image/png, application/pdf, video/mp4 etc
    @Column(nullable = false)
    private String fileType;

    // ⭐ ACTUAL FILE STORED IN DB ⭐
    @Lob
    @Basic(fetch = FetchType.LAZY) // VERY IMPORTANT
    @Column(columnDefinition = "LONGBLOB")
    private byte[] fileData;

    public LeaveProof() {}

    public LeaveProof(LeaveRequest leaveRequest,
                      String fileName,
                      String fileType,
                      byte[] fileData) {
        this.leaveRequest = leaveRequest;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileData = fileData;
    }

    // ===== Getters & Setters =====

    public Integer getId() {
        return id;
    }

    public LeaveRequest getLeaveRequest() {
        return leaveRequest;
    }

    public void setLeaveRequest(LeaveRequest leaveRequest) {
        this.leaveRequest = leaveRequest;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
}
