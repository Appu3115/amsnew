package com.example.amsnew.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.amsnew.dto.DepartmentAttendanceDTO;
import com.example.amsnew.dto.LoginRequest;
import com.example.amsnew.model.Attendance;
import com.example.amsnew.model.AttendanceStatus;
import com.example.amsnew.model.Employees;
import com.example.amsnew.model.Shift;
import com.example.amsnew.repository.AttendanceRepository;
import com.example.amsnew.repository.UserRepository;
import com.example.amsnew.util.DateUtil;


@Service
public class AttendanceService {
       @Autowired
       private AttendanceRepository attendanceRepo;
       
       @Autowired
    	private UserRepository userrepo;

 
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
    	   attendance.setAttendanceDate(today);
    	   attendance.setLogin(time);
    	 
    	   Employees emp = userrepo.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"));

    		attendance.setEmployee(emp);       
    		attendance.setEmployeeId(employeeId); 
    		
    	   Shift shift=emp.getShift();
    	   if(shift == null)
    	   {
    		   return ResponseEntity.badRequest().body("Shift not assigned to employee");
    	   }
    	   LocalTime loginTime=time.toLocalTime();
    	   
    	   LocalTime shiftStart=shift.getStartTime().plusMinutes(shift.getGraceMinutes());
    	   
    	   if(loginTime.isAfter(shiftStart))
    	   {
    		   attendance.setStatus(AttendanceStatus.LATE);
    	   }
    	   else {
    		   attendance.setStatus(AttendanceStatus.PRESENT);
    	   }
    	   
    	   Attendance saved=attendanceRepo.save(attendance);
    	   return ResponseEntity.ok(saved);
    	   
       }

       
       public ResponseEntity<?> logoutByEmployeeId(String employeeId) {

    	    Optional<Attendance> optionalAttendance =
    	        attendanceRepo.findByEmployeeIdAndLogoutIsNull(employeeId);

    	    if (!optionalAttendance.isPresent()) {
    	        return ResponseEntity.badRequest()
    	            .body("No active login found for employee");
    	    }

    	    Attendance attendance = optionalAttendance.get();

    	    LocalDateTime logoutTime = LocalDateTime.now();
    	    attendance.setLogout(logoutTime);
    	    attendance.setStatus(AttendanceStatus.LOGGED_OUT);

    	    Shift shift = attendance.getEmployee().getShift();

    	    if (shift != null) {
    	        LocalDateTime shiftEnd = LocalDateTime.of(
    	            attendance.getAttendanceDate(),
    	            shift.getEndTime()
    	        );

    	        if (shift.getEndTime().isBefore(shift.getStartTime())) {
    	            shiftEnd = shiftEnd.plusDays(1);
    	        }

    	        long overtime = logoutTime.isAfter(shiftEnd)
    	            ? Duration.between(shiftEnd, logoutTime).toMinutes()
    	            : 0;

    	        attendance.setOvertime(overtime);
    	    }

    	    Attendance saved = attendanceRepo.save(attendance);
    	    return ResponseEntity.ok(saved);
    	}


       public ResponseEntity<?>  fetchAttendance(String employeeId,String date)
       {
    	   
    	   LocalDate parsedDate =null;
    	   
    	   if(date != null && !date.isBlank())
    	   {
    		   parsedDate =DateUtil.parseDate(date);
    	   }
    	   
    	   if(employeeId == null && parsedDate==null)
    	   {
    		   List<Attendance> list=attendanceRepo.findAll();
    		   
    		   if(list.isEmpty())
    		   {
    			   return ResponseEntity.ok("No attendance record found");
    		   }
    		   return ResponseEntity.ok(list);
    	   }
    	   if(employeeId == null && parsedDate != null)
    	   {
    		   List<Attendance> list=attendanceRepo.findByAttendanceDate(parsedDate);
    		   if(list.isEmpty())
    		   {
    			   return ResponseEntity.badRequest().body("No attendance found for date: "+ parsedDate);
    		   }
    		   return ResponseEntity.ok(list);
    	   }
    	   
    	   if(employeeId != null && parsedDate == null)
    	   {
    		   List<Attendance> list=attendanceRepo.findByEmployeeId(employeeId);
    		   
    		   if(list.isEmpty())
    		   {
    			   return ResponseEntity.badRequest().body("No attendance found for employeeId: "+ employeeId);
    		   }
    		   return ResponseEntity.ok(list);
    	   }
    	   
    	   Optional <Attendance> attendance=attendanceRepo.findByEmployeeIdAndAttendanceDate(employeeId, parsedDate);
    	   
    	   if(attendance.isEmpty())
    	   {
    		   return ResponseEntity.badRequest().body("No attendance found for employeeId: "+ employeeId+" No attendance found for date: "+ date);
    	   }
    	   
    	   return ResponseEntity.ok(attendance.get());
       }
       
       public List<DepartmentAttendanceDTO> getDepartmentWiseAttendance() {
    	    return attendanceRepo.departmentWiseAttendance();
    	}

}
