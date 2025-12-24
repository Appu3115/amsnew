package com.example.amsnew.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.amsnew.dto.DepartmentAttendanceDTO;
import com.example.amsnew.model.Attendance;
import com.example.amsnew.model.AttendanceStatus;



@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long>{
     List<Attendance> findByEmployeeId(String employeeId);
     
     boolean existsByEmployeeIdAndLogin(String employeeId,LocalDateTime login);
     
     Optional <Attendance> findByEmployeeIdAndAttendanceDate(String employeeId,LocalDate attendance);
     
     List<Attendance> findByAttendanceDate(LocalDate attendanceDate); 
     
     Optional <Attendance> findById(Long id);
     
    

     Optional<Attendance> findByEmployeeIdAndLogoutIsNull(
    		    String employeeId
    		);

  

    	    long countByEmployee_Department_IdAndAttendanceDateAndStatus(
    	        Long departmentId,
    	        LocalDate attendanceDate,
    	        AttendanceStatus status
    	    );
    	

}
