package com.example.amsnew.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.amsnew.model.LeaveRequest;



@Repository
public interface LeaveRequestRepo extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployee_Id(int id);

    List<LeaveRequest> findAllByStatus(String approved);
}

