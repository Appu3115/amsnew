package com.example.amsnew.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.amsnew.dto.AssignShiftRequest;
import com.example.amsnew.service.EmployeeShiftsService;

@RestController
@RequestMapping("/employeeshift")
@CrossOrigin(origins = "*")
public class EmployeeShiftController {

    @Autowired
    private EmployeeShiftsService employeeShiftsService;

    /**
     * Assign or Update shift for an employee
     */
    @PostMapping("/assign")
    public ResponseEntity<?> assignShift(@RequestBody AssignShiftRequest request) {
     return employeeShiftsService.assignShift(request);
//        
    }
    
    @PutMapping("/update")
    public ResponseEntity<?> updateShift(@RequestBody AssignShiftRequest request) {
    	return employeeShiftsService.updateShift(request);
    }
}