package com.example.amsnew.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.amsnew.dto.LoginRequest;
import com.example.amsnew.model.Attendance;
import com.example.amsnew.service.AttendanceService;



@RestController
@RequestMapping("/attendance")
public class AttendanceController {

	@Autowired
	private AttendanceService attendanceService;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request)
	{
		return attendanceService.login(request);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestParam String employeeId) {
	    return attendanceService.logoutByEmployeeId(employeeId);
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


}
