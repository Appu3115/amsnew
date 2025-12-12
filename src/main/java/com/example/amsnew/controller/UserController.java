package com.example.amsnew.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.amsnew.dto.LoginRequest;
import com.example.amsnew.dto.RegisterDTO;
import com.example.amsnew.service.UserService;



@RestController
@RequestMapping("/user")
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
}
