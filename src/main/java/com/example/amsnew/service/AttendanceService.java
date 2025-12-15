package com.example.amsnew.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.amsnew.dto.LoginRequest;
import com.example.amsnew.model.Attendance;
import com.example.amsnew.repository.AttendanceRepository;


@Service
public class AttendanceService {
       @Autowired
       private AttendanceRepository attendanceRepo;
 
       public ResponseEntity<?> login( LoginRequest request)
       {
    	   if(request == null || request.getEmployeeId() == null ||request.getEmployeeId().trim().isEmpty())
    	   {
    		   return ResponseEntity.badRequest().body("Employee ID is required");
    	   }
    	   
    	   String employeeId=request.getEmployeeId();
    	   LocalDateTime time=LocalDateTime.now();
    	   LocalDate today=LocalDate.now();
    	   
    	   Optional<Attendance>existingAttendance =attendanceRepo.findByEmployeeIdAndAttendanceDate(employeeId, today);
    	   if(existingAttendance.isPresent())
    	   {
    		   return ResponseEntity.badRequest().body("You already Logged in today");
    	   }
    	   Attendance attendance=new Attendance();
    	   attendance.setEmployeeId(employeeId);
    	   attendance.setAttendanceDate(today);
    	   attendance.setLogin(time);
    	   attendance.setStatus("Logged In");
    	   
    	   Attendance saved=attendanceRepo.save(attendance);
    	   
    	   
    	   return ResponseEntity.ok(saved);
    	   
       }
       public ResponseEntity<?> logout(Long id)
       {
    	   Optional<Attendance> optionalAttendance =attendanceRepo.findById(id);
    	   
    	   if(!optionalAttendance.isPresent())
    	   {
    		   return ResponseEntity.badRequest().body("Attendance record not found");
    	   }
    	   
    	   Attendance attendance=optionalAttendance.get();
    	   
    	   if(attendance.getLogout() != null)
    	   {
    		   return ResponseEntity.badRequest().body("Employee already logged out");
    	   }
    	    
    	   LocalDateTime time=LocalDateTime.now();
    	   
    	   attendance.setLogout(time);
    	   attendance.setStatus("Logged out");
    	   
    	   Attendance saved=attendanceRepo.save(attendance);
    	   
    	   return ResponseEntity.ok(saved);
       }
       
       public List<Attendance> getAttendanceByEmployee(String employeeId)
       {
    	   return attendanceRepo.findByEmployeeId(employeeId);
       }
       
       public ResponseEntity<?>  getAttendanceByDate(String employeeId,LocalDate date)
       {
    	   if(employeeId == null || date== null)
    	   {
    		   return ResponseEntity.badRequest().body("Employee Id and Date are required");
    	   }
    	   
    	   Optional<Attendance> attendance=attendanceRepo.findByEmployeeIdAndAttendanceDate(employeeId, date);
    	   
    	   if(!attendance.isPresent())
    	   {
    		   return ResponseEntity.badRequest().body("No attendance found for this date");
    	   }
    	   
    	   return ResponseEntity.ok(attendance.get());
       }
}
