package com.example.amsnew.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
	@PostMapping("/logout/{id}")
	public ResponseEntity<?>  logout(@PathVariable Long id)
	{
		return attendanceService.logout(id);
	}
	
	@GetMapping("/{employeeId}")
	public List<Attendance>  getByEmployee(@PathVariable String employeeId)
	{
		return attendanceService.getAttendanceByEmployee(employeeId);
	}
}
