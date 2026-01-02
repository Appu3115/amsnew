package com.example.amsnew.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.amsnew.dto.LoginRequest;
import com.example.amsnew.model.ActivityType;
import com.example.amsnew.model.Attendance;
import com.example.amsnew.model.SessionType;
import com.example.amsnew.service.AttendanceService;



@RestController
@RequestMapping("/attendance")
@CrossOrigin(origins="*")
public class AttendanceController {

	@Autowired
	private AttendanceService attendanceService;
	

	@PostMapping("/login")
	public ResponseEntity<?> login(
	        @RequestParam String employeeId,
	        @RequestParam(defaultValue = "false") boolean workFromHome
	) {
	    return attendanceService.login(employeeId, workFromHome);
	}


	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestParam String employeeId) {
	    return attendanceService.logoutByEmployeeId(employeeId);
	}

	// Attendance History For Admin
	@GetMapping("/admin/daily")
	public ResponseEntity<?> getAllEmployeesDailyAttendance(
	        @RequestParam(required = false) String fromDate,
	        @RequestParam(required = false) String toDate
	) {
	    return attendanceService
	            .getAllEmployeesDailyAttendance(fromDate, toDate);
	}


	 
	@GetMapping("/fetch")
	public ResponseEntity<?>  fetchAttendance(@RequestParam(required=false) 
	String employeeId,@RequestParam(required=false) 
	@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)String date)
	{
		return attendanceService.fetchAttendance(employeeId, date);
	}
	
	
	
	@GetMapping("/department-wise")
	public ResponseEntity<?> departmentWiseAttendance(
	        @RequestParam(required = false)
	        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	        LocalDate date
	) {
	    return ResponseEntity.ok(
	        attendanceService.getDepartmentWiseAttendance(
	            date != null ? date : LocalDate.now()
	        )
	    );
	}


	
	// BREAK/ LUNCH

	@PostMapping("/pause")
    public ResponseEntity<?> pause(
            @RequestParam String employeeId,
            @RequestParam SessionType type   // BREAK or LUNCH
    ) {
        return attendanceService.startPause(employeeId, type);
    }
	
	//RESUME WORK
	
	@PostMapping("/resume")
    public ResponseEntity<?> resume(@RequestParam String employeeId) {
        return attendanceService.resumeWork(employeeId);
    }
	
	// ACTIVITY TRACKING
	
	@PostMapping("/activity")
    public void recordActivity(@RequestParam String employeeId,@RequestParam ActivityType type  ) {
        attendanceService.recordActivity(employeeId, type);
    }
	
	//PRODUCTIVE TIME
	
	@GetMapping("/productive-time")
    public ResponseEntity<?> productiveTime( @RequestParam String employeeId,@RequestParam LocalDate date) {
        return ResponseEntity.ok(
                attendanceService.calculateProductiveTime(employeeId, date)
        );
    }
	
	
	//Total work time 
	@GetMapping("/total-work-time")
	public ResponseEntity<?> totalWorkTime( @RequestParam String employeeId, @RequestParam LocalDate date) {
	    return ResponseEntity.ok(
	            attendanceService.calculateTotalWorkTime(employeeId, date)
	    );
	}

	//Idle time
	@GetMapping("/idle-time")
	public ResponseEntity<?> idleTime( @RequestParam String employeeId, @RequestParam LocalDate date) {
	    return ResponseEntity.ok(
	            attendanceService.calculateIdleTime(employeeId, date)
	    );
	}

	// PERMISSION REQUEST
	@PostMapping("/permission")
	public ResponseEntity<?> requestPermission(@RequestParam String employeeId,@RequestParam long minutes) {

	    return attendanceService.requestPermission(employeeId, minutes);
	}

	// TOTAL BREAK TIME
	@GetMapping("/break-time")
	public ResponseEntity<?> breakTime( @RequestParam String employeeId,  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

	    return ResponseEntity.ok(
	            attendanceService.calculateTotalBreakTime(employeeId, date)
	    );
	}

	//Attendance Summary for employee side
	@GetMapping("/dashboard/employee")
	public ResponseEntity<?> employeeDashboard(
	        @RequestParam String employeeId,
	        @RequestParam int year,
	        @RequestParam int month) {

	    return attendanceService
	            .getEmployeeAttendanceSummary(employeeId, year, month);
	}

	//attendance summary for admin side
	@GetMapping("/dashboard/admin")
	public ResponseEntity<?> adminDashboard(
	        @RequestParam(required = false)
	        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	        LocalDate date) {

	    return attendanceService.getAdminAttendanceDashboard(date);
	}




}
