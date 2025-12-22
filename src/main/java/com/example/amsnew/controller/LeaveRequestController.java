package com.example.amsnew.controller;

import java.util.Collections;
import java.util.List;


import com.example.amsnew.dto.LeaveRequestDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.amsnew.model.LeaveRequest;
import com.example.amsnew.service.LeaveRequestService;



@RestController
@RequestMapping("/leave")
@CrossOrigin(origins="*")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService service;


//    @GetMapping("/getAllLeaveDetails")
//    public ResponseEntity<List<LeaveRequest>> getAllLeave(){
//        List<LeaveRequest> leaves = service.getAllLeave();
//        if (leaves.isEmpty()) {
//            return ResponseEntity.ok(Collections.emptyList());
//        }
//        return ResponseEntity.ok(leaves);
//    }
@GetMapping("/getAllLeaveDetails")
public ResponseEntity<List<LeaveRequestDTO>> getAllLeave() {
    List<LeaveRequestDTO> leaves = service.getAllLeave();
    if (leaves.isEmpty()) {
        return ResponseEntity.ok(Collections.emptyList());
    }
    return ResponseEntity.ok(leaves);
}


    @PostMapping("/applyLeave")
    public ResponseEntity<String> applyLeave(@Valid @RequestBody LeaveRequest request) {
        try {
            service.applyLeave(request);
            return ResponseEntity.ok("Leave applied");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getLeaveDetailsOfEmployee/{employeeId}")
    public ResponseEntity<List<LeaveRequestDTO>> getAllLeaveById(@PathVariable int employeeId){
        List<LeaveRequestDTO> leaves = service.getAllLeaveByEmployeeId(employeeId);
        if (leaves.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(leaves);
    }

    @GetMapping("/getLeaveDetails/{id}")
    public ResponseEntity<?> getLeaveById(@PathVariable int id){

        try{
            LeaveRequestDTO leave = service.getLeaveById(id);
            return ResponseEntity.ok(leave);
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/approveLeave/{id}")
    public ResponseEntity<String> approveLeave(@PathVariable int id){
        try{
            LeaveRequest leave =  service.approveLeave(id);
            return new ResponseEntity<>("Leave Approved", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/rejectLeave/{id}")
    public ResponseEntity<String> rejectLeave(@PathVariable int id){
        try{
            LeaveRequest leave =  service.rejectLeave(id);
            return new ResponseEntity<>("Leave Rejected", HttpStatus.OK);
        } catch (Exception e) {

            return new ResponseEntity<>("unable to reject leave", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/approvedLeave")
    public ResponseEntity<List<LeaveRequestDTO>> getApprovedLeave() {
        List<LeaveRequestDTO> leaves = service.getLeaveStatus("approved");
        return ResponseEntity.ok(leaves); // returns empty list if none
    }

    @GetMapping("/rejectedLeave")
    public ResponseEntity<List<LeaveRequestDTO>> getRejectedLeave() {
        List<LeaveRequestDTO> leaves = service.getLeaveStatus("rejected");
        return ResponseEntity.ok(leaves);
    }

    @GetMapping("/pendingLeave")
    public ResponseEntity<List<LeaveRequestDTO>> getPendingLeave(){
        List<LeaveRequestDTO> leaves = service.getLeaveStatus("pending");
        return ResponseEntity.ok(leaves);
    }
}
