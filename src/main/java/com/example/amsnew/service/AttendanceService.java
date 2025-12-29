package com.example.amsnew.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.amsnew.dto.DepartmentAttendanceDTO;
import com.example.amsnew.dto.LoginRequest;
import com.example.amsnew.model.Attendance;
import com.example.amsnew.model.AttendanceStatus;
import com.example.amsnew.model.Department;
import com.example.amsnew.model.Employees;
import com.example.amsnew.model.Shift;
import com.example.amsnew.repository.AttendanceRepository;
import com.example.amsnew.repository.DepartmentRepository;
import com.example.amsnew.repository.UserRepository;
import com.example.amsnew.util.DateUtil;


@Service
public class AttendanceService {
       @Autowired
       private AttendanceRepository attendanceRepo;
       
       @Autowired
    	private UserRepository userrepo;
       
       @Autowired
       private DepartmentRepository departmentRepo;

 
       public ResponseEntity<?> login(String EmployeeId) {

    	    if (EmployeeId == null || EmployeeId.isBlank()) {
    	        return ResponseEntity.badRequest().body("Employee ID is required");
    	    }

    	    LocalDate today = LocalDate.now();
    	    LocalDateTime loginTime = LocalDateTime.now();

    	    Optional<Attendance> existingAttendance =
    	            attendanceRepo.findByEmployeeIdAndAttendanceDate(EmployeeId, today);

    	    if (existingAttendance.isPresent()) {
    	        return ResponseEntity.badRequest().body("You already logged in today");
    	    }

    	    Employees emp = userrepo.findByEmployeeId(EmployeeId)
    	            .orElseThrow(() -> new RuntimeException("Employee not found"));

    	    Shift shift = emp.getShift();
    	    if (shift == null) {
    	        return ResponseEntity.badRequest().body("Shift not assigned to employee");
    	    }

    	    Attendance attendance = new Attendance();
    	    attendance.setEmployee(emp);
    	    attendance.setEmployeeId(EmployeeId);
    	    attendance.setAttendanceDate(today);
    	    attendance.setLogin(loginTime);

    	    LocalTime shiftStartWithGrace =
    	            shift.getStartTime().plusMinutes(shift.getGraceMinutes());

    	    AttendanceStatus status =
    	            loginTime.toLocalTime().isAfter(shiftStartWithGrace)
    	                    ? AttendanceStatus.LATE
    	                    : AttendanceStatus.PRESENT;

    	    attendance.setStatus(status);

    	    Attendance saved = attendanceRepo.save(attendance);

    	    // ✅ Clean response for frontend
    	    Map<String, Object> response = new HashMap<>();
    	    response.put("message", "Login successful");
    	    response.put("attendanceDate", saved.getAttendanceDate());
    	    response.put("loginTime", saved.getLogin());
    	    response.put("status", saved.getStatus());

    	    return ResponseEntity.ok(response);
    	}


       
       public ResponseEntity<?> logoutByEmployeeId(String employeeId) {

    	    Optional<Attendance> optionalAttendance =
    	            attendanceRepo.findByEmployeeIdAndLogoutIsNull(employeeId);

    	    if (optionalAttendance.isEmpty()) {
    	        return ResponseEntity.badRequest()
    	                .body("No active login found for employee");
    	    }

    	    Attendance attendance = optionalAttendance.get();
    	    Shift shift = attendance.getEmployee().getShift();

    	    if (shift == null) {
    	        return ResponseEntity.badRequest().body("Shift not assigned");
    	    }

    	    LocalDateTime now = LocalDateTime.now();

    	    // 🔹 Calculate shift end datetime
    	    LocalDateTime shiftEnd = LocalDateTime.of(
    	            attendance.getAttendanceDate(),
    	            shift.getEndTime()
    	    );

    	    // 🔹 Night shift handling
    	    if (shift.getEndTime().isBefore(shift.getStartTime())) {
    	        shiftEnd = shiftEnd.plusDays(1);
    	    }

    	    // ❌ BLOCK logout if shift not completed
    	    if (now.isBefore(shiftEnd)) {
    	        return ResponseEntity.badRequest().body(
    	                "Logout disabled until shift ends at " + shiftEnd.toLocalTime()
    	        );
    	    }

    	    // ✅ Allow logout
    	    attendance.setLogout(now);
    	    attendance.setStatus(AttendanceStatus.LOGGED_OUT);

    	    long overtime = now.isAfter(shiftEnd)
    	            ? Duration.between(shiftEnd, now).toMinutes()
    	            : 0;

    	    attendance.setOvertime(overtime);

    	    Attendance saved = attendanceRepo.save(attendance);

    	    // ✅ Clean response for frontend
    	    Map<String, Object> response = new HashMap<>();
    	    response.put("message", "Logout successful");
    	    response.put("logoutTime", saved.getLogout());
    	    response.put("overtimeMinutes", saved.getOvertime());

    	    return ResponseEntity.ok(response);
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
       
       
       public ResponseEntity<?> fetchAttendanceById(String employeeId) {

    	    // 🔹 Fetch all attendance
    	    if (employeeId == null || employeeId.trim().isEmpty()) {
    	        List<Attendance> list = attendanceRepo.findAll();

    	        if (list.isEmpty()) {
    	            return ResponseEntity.ok(Collections.emptyList());
    	        }
    	        return ResponseEntity.ok(list);
    	    }

    	    // 🔹 Fetch by employeeId
    	    List<Attendance> list = attendanceRepo.findByEmployeeId(employeeId);

    	    if (list.isEmpty()) {
    	        return ResponseEntity
    	                .badRequest()
    	                .body("No attendance found for employeeId: " + employeeId);
    	    }

    	    return ResponseEntity.ok(list);
    	}

       
       
       
       public List<DepartmentAttendanceDTO> getDepartmentWiseAttendance(LocalDate date) {

    	    List<Department> departments = departmentRepo.findAll();
    	    List<DepartmentAttendanceDTO> result = new ArrayList<>();

    	    for (Department dept : departments) {

    	        long totalEmployees =
    	            userrepo.countByDepartmentId(dept.getId());

    	        long present =
    	            attendanceRepo.countByEmployee_Department_IdAndAttendanceDateAndStatus(
    	                dept.getId(), date, AttendanceStatus.PRESENT
    	            );

    	        long late =
    	            attendanceRepo.countByEmployee_Department_IdAndAttendanceDateAndStatus(
    	                dept.getId(), date, AttendanceStatus.LATE
    	            );

    	        long absent = totalEmployees - (present + late);

    	        result.add(
    	            new DepartmentAttendanceDTO(
    	                dept.getDeptName(),
    	                totalEmployees,
    	                present,
    	                late,
    	                absent
    	            )
    	        );
    	    }

    	    return result;
    	}



}
