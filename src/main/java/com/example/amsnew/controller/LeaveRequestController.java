package com.example.amsnew.controller;


import com.example.amsnew.dto.LeaveRequestDTO;
import com.example.amsnew.service.LeaveRequestService;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/leave")
@CrossOrigin(origins = "*")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService service;


    @PostMapping("/applyleave")
    public ResponseEntity<?> applyLeave(
            @RequestParam String employeeId,
            @RequestBody LeaveRequestDTO dto
    ) {
        return service.applyLeave(employeeId, dto);
    }

    
    @GetMapping("/request/getall")
    public ResponseEntity<?> getAllLeaves() {
        return service.getAllEmployeesLeaveRequests();
    }
    
    
    // 3️⃣ Approve Leave (Admin)
    @PutMapping("/approve/{leaveId}")
    public ResponseEntity<?> approveLeave(
            @PathVariable Integer leaveId
    ) {
        return service.approveLeave(leaveId);
    }

    // 4️⃣ Reject Leave (Admin) – OPTIONAL BUT RECOMMENDED
    @PutMapping("/reject/{leaveId}")
    public ResponseEntity<?> rejectLeave(
            @PathVariable Integer leaveId
    ) {
        return service.rejectLeave(leaveId);
    }

    // 5️⃣ Get leave balance (Employee)
    @GetMapping("/balance/{employeeId}")
    public ResponseEntity<?> getLeaveBalance(
            @PathVariable String employeeId
    ) {
        return service.getLeaveBalance(employeeId);
    }
}
