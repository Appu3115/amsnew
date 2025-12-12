package com.example.amsnew.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.amsnew.dto.LoginRequest;
import com.example.amsnew.model.Attendance;
import com.example.amsnew.model.EmployeeShifts;
import com.example.amsnew.repository.AttendanceRepository;
import com.example.amsnew.repository.EmployeeShiftsRepository;

import io.swagger.v3.oas.annotations.parameters.RequestBody;


@Service
public class AttendanceService {
       @Autowired
       private AttendanceRepository attendanceRepo;
       
       @Autowired
       private EmployeeShiftsRepository employeeShiftsRepo;
       
       public ResponseEntity<?> login( LoginRequest request)
       {
    	   if(request == null || request.getEmployeeId() == null ||request.getEmployeeId().trim().isEmpty())
    	   {
    		   return ResponseEntity.badRequest().body("Employee ID is required");
    	   }
    	   String empId =request.getEmployeeId();
    	   
           LocalDateTime time=LocalDateTime.now();
           
          List<EmployeeShifts> shifts=employeeShiftsRepo.findByEmployeeId(empId);
           
           String shiftId=null;
           
           if(!shifts.isEmpty())
           {
        	   EmployeeShifts lastShift=shifts.get(shifts.size()-1);
        	   shiftId=lastShift.getShiftId();
           }

    	   
    	   Attendance attendance=new Attendance();
    	   attendance.setEmployeeId(request.getEmployeeId());
    	  attendance.setShiftId(shiftId);
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
       
      
}
