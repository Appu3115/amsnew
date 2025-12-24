package com.example.amsnew.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.amsnew.model.LeaveProof;

public interface LeaveProofRepository extends JpaRepository<LeaveProof, Integer> {

    // 📎 Get all proofs for a leave request
    List<LeaveProof> findByLeaveRequestId(Long leaveId);
}