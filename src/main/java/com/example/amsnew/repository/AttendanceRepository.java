package com.example.amsnew.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.amsnew.model.Attendance;



@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long>{
     List<Attendance> findByEmployeeId(String employeeId);
     
     boolean existsByEmployeeIdAndLogin(String employeeId,LocalDateTime login);
}
