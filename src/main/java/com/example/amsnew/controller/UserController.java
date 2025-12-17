package com.example.amsnew.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.amsnew.dto.LoginRequest;
import com.example.amsnew.dto.RegisterDTO;
import com.example.amsnew.service.UserService;



@RestController
@RequestMapping("/user")
@CrossOrigin(origins="*")
public class UserController {

	@Autowired
	private UserService userservice;
	
	@PostMapping("/register")
	public ResponseEntity<?> createEmployee(@RequestBody RegisterDTO request){
		return userservice.createEmployee(request);
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<?> loginEmp(@RequestBody LoginRequest request){
		return userservice.loginEmployee(request);
	}
	
	@GetMapping("/getAllEmployees")
	public ResponseEntity<?> getAllEmployees(){
		return userservice.getAllEmployees();
	}
	
	@DeleteMapping("/delete/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable String employeeId) {
        return userservice.deleteEmployee(employeeId);
    }

    
    @PutMapping("/deactivate/{employeeId}")
    public ResponseEntity<?> deactivateEmployee(@PathVariable String employeeId) {
        return userservice.deactivateEmployee(employeeId);
    }
}
