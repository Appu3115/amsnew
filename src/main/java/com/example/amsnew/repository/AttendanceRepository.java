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



@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long>{
     List<Attendance> findByEmployeeId(String employeeId);
     
     boolean existsByEmployeeIdAndLogin(String employeeId,LocalDateTime login);
     
     Optional <Attendance> findByEmployeeIdAndAttendanceDate(String employeeId,LocalDate attendance);
     
     List<Attendance> findByAttendanceDate(LocalDate attendanceDate); 
     
     Optional <Attendance> findById(Long id);
     
     @Query("""
    		 SELECT new com.example.amsnew.dto.DepartmentAttendanceDTO(
    		     d.deptName,
    		     SUM(CASE WHEN a.status = com.example.amsnew.model.AttendanceStatus.PRESENT THEN 1 ELSE 0 END),
    		     SUM(CASE WHEN a.status = com.example.amsnew.model.AttendanceStatus.ABSENT THEN 1 ELSE 0 END)
    		 )
    		 FROM Attendance a
    		 JOIN a.employee e
    		 JOIN e.department d
    		 GROUP BY d.deptName
    		 """)
    		 List<DepartmentAttendanceDTO> departmentWiseAttendance();

    
}
