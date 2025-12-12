package com.example.amsnew.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.amsnew.repository.EmployeeShiftsRepository;

@Service
public class EmployeeShiftsService {

	@Autowired
	private EmployeeShiftsRepository employeeshiftsrepo;
	
	
}
