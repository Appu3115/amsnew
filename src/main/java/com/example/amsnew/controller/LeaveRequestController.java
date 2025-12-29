package com.example.amsnew.controller;

import java.util.Collections;
import java.util.List;

import com.example.amsnew.dto.LeaveRequestDTO;
import com.example.amsnew.model.LeaveRequest;
import com.example.amsnew.model.LeaveStatus;
import com.example.amsnew.service.LeaveRequestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/leave")
@CrossOrigin(origins = "*")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService service;

    /* ================= GET ALL LEAVES ================= */
//    @GetMapping("/getAllLeaveDetails")
//    public ResponseEntity<List<LeaveRequestDTO>> getAllLeave() {
//        List<LeaveRequestDTO> leaves = service.getAllLeave();
//        return ResponseEntity.ok(
//                leaves.isEmpty() ? Collections.emptyList() : leaves
//        );
//    }

    /* ================= APPLY LEAVE (WITH FILES) ================= */
    @PostMapping(
            value = "/applyleave",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> applyLeave(@RequestParam String employeeId,
            @RequestPart("leave") LeaveRequestDTO request,
            @RequestPart(value = "files", required = false) MultipartFile[] files
    ) {
        return service.applyLeave(employeeId,request, files);
    }

    /* ================= GET LEAVES BY EMPLOYEE ================= */
//    @GetMapping("/getLeaveDetailsOfEmployee/{employeeId}")
//    public ResponseEntity<List<LeaveRequestDTO>> getAllLeaveByEmployeeId(
//            @PathVariable int employeeId
//    ) {
//        List<LeaveRequestDTO> leaves = service.getAllLeaveByEmployeeId(employeeId);
//        return ResponseEntity.ok(
//                leaves.isEmpty() ? Collections.emptyList() : leaves
//        );
//    }
//
//    /* ================= GET LEAVE BY ID ================= */
//    @GetMapping("/getLeaveDetails/{id}")
//    public ResponseEntity<?> getLeaveById(@PathVariable Integer id) {
//        try {
//            LeaveRequestDTO leave = service.getLeaveById(id);
//            return ResponseEntity.ok(leave);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    /* ================= APPROVE LEAVE ================= */
//    @PutMapping("/approveLeave/{id}")
//    public ResponseEntity<String> approveLeave(@PathVariable Integer id) {
//        try {
//            service.approveLeave(id);
//            return ResponseEntity.ok("Leave Approved");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(e.getMessage());
//        }
//    }
//
//    /* ================= REJECT LEAVE ================= */
//    @PutMapping("/rejectLeave/{id}")
//    public ResponseEntity<String> rejectLeave(@PathVariable Integer id) {
//        try {
//            service.rejectLeave(id);
//            return ResponseEntity.ok("Leave Rejected");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body("Unable to reject leave");
//        }
//    }
//
//    /* ================= FILTER BY STATUS ================= */
//    @GetMapping("/approvedLeave")
//    public ResponseEntity<List<LeaveRequestDTO>> getApprovedLeave() {
//        return ResponseEntity.ok(service.getLeaveStatus(LeaveStatus.APPROVED));
//    }
//
//    @GetMapping("/rejectedLeave")
//    public ResponseEntity<List<LeaveRequestDTO>> getRejectedLeave() {
//        return ResponseEntity.ok(service.getLeaveStatus(LeaveStatus.REJECTED));
//    }
//
//    @GetMapping("/pendingLeave")
//    public ResponseEntity<List<LeaveRequestDTO>> getPendingLeave() {
//        return ResponseEntity.ok(service.getLeaveStatus(LeaveStatus.PENDING));
//    }
}
