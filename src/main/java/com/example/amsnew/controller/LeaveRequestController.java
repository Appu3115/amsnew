package com.example.amsnew.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.amsnew.model.LeaveRequest;
import com.example.amsnew.service.LeaveRequestService;



@RestController
@RequestMapping("/leave")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService service;


    @GetMapping("/getLeaveDetails")
    public ResponseEntity<List<LeaveRequest>> getAllLeave(){
        List<LeaveRequest> leaves = service.getAllLeave();
        if(leaves != null){
            return new ResponseEntity<>(leaves, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

        @PostMapping("/applyLeave")
        public ResponseEntity<String> applyLeave(@RequestBody LeaveRequest request){
            LeaveRequest leave = service.applyLeave(request);
            if(leave != null){
                return ResponseEntity.ok("Leave applied");
            }
            else{
                return ResponseEntity.badRequest().body("something went wrong");
            }
        }

    @GetMapping("/getLeaveDetails/{employeeId}")
    public ResponseEntity<List<LeaveRequest>> getAllLeaveById(@PathVariable int employeeId){
        List<LeaveRequest> leaves = service.getAllLeaveById(employeeId);
        if(leaves != null && !leaves.isEmpty()){
            return new ResponseEntity<>(leaves, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/approveLeave/{id}")
    public ResponseEntity<String> approveLeave(@PathVariable int id){
        try{
            LeaveRequest leave =  service.approveLeave(id);
            return new ResponseEntity<>("success", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/rejectLeave/{id}")
    public ResponseEntity<String> rejectLeave(@PathVariable int id){
        try{
            LeaveRequest leave =  service.rejectLeave(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/approvedLeave")
    public ResponseEntity<List<LeaveRequest>> getApprovedLeave(){
        List<LeaveRequest> leaves =  service.getLeaveStatus("approved");
        if(leaves != null){
            return new ResponseEntity<>(leaves, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/rejectedLeave")
    public List<LeaveRequest> getrejectedLeave(){
        return service.getLeaveStatus("rejected");
    }

    @GetMapping("/pendingLeave")
    public List<LeaveRequest> getPendingLeave(){
        return service.getLeaveStatus("pending");
    }
}
