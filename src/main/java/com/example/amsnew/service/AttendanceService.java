package com.example.amsnew.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.amsnew.model.Attendance;
import com.example.amsnew.repository.AttendanceRepository;


@Service
public class AttendanceService {
       @Autowired
       private AttendanceRepository attendanceRepo;
       
       public ResponseEntity<?> login(Attendance request)
       {
    	   if(request == null)
    	   {
    		   return ResponseEntity.badRequest().body("Request Body is missing");
    	   }
    	   String empId =trimOrNull(request.getEmployeeId());
    	   if(empId == null || empId.isEmpty())
    	   {
    		   return ResponseEntity.badRequest().body("Employee ID is required");
    	   }
    	   
    	   LocalDateTime time=LocalDateTime.now();
    	   LocalDateTime todaystart=time.toLocalDate().atStartOfDay();
    	   
    	   if(attendanceRepo.existsByEmployeeIdAndLogin(empId, todaystart))
    	   {
    		   return ResponseEntity.badRequest().body("Employee has already logged in today");
    	   }
    	   
    	   Attendance attendance=new Attendance();
    	   attendance.setEmployeeId(empId);
    	   attendance.setShiftId(request.getShiftId());
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
    	   
    	   attendanceRepo.save(attendance);
    	   
    	   return ResponseEntity.ok(attendance);
       }
       
       public List<Attendance> getAttendanceByEmployee(String employeeId)
       {
    	   return attendanceRepo.findByEmployeeId(employeeId);
       }
       
       private String trimOrNull(String value)
       {
    	   return value == null? null:value.trim();
       }
}
