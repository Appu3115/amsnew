package com.example.amsnew.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.example.amsnew.dto.LeaveRequestDTO;
import com.example.amsnew.model.*;
import com.example.amsnew.repository.LeaveProofRepository;
import com.example.amsnew.repository.LeaveRequestRepo;
import com.example.amsnew.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepo repo;

    @Autowired
    private UserRepository userrepo;

    @Autowired
    private LeaveProofRepository proofrepo;

  
    public ResponseEntity<?> applyLeave(
            String employeeId,
            LeaveRequestDTO request   // 👈 NEW DTO
    ) {

        Map<String, Object> response = new HashMap<>();

        // 👤 Get employee
        Optional<Employees> empOpt = userrepo.findByEmployeeId(employeeId);
        if (empOpt.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Employee Not Found");
        }

        Employees emp = empOpt.get();

        // 📅 Date validation
        if (request.getStartDate().isAfter(request.getEndDate())) {
            return ResponseEntity
                    .badRequest()
                    .body("Start date cannot be after end date");
        }

        if (request.getStartDate().isBefore(LocalDate.now())) {
            return ResponseEntity
                    .badRequest()
                    .body("Leave cannot start in the past");
        }

        // ❌ Overlap check
        boolean overlap = repo.existsOverlappingLeave(
                emp.getId(),
                request.getStartDate(),
                request.getEndDate()
        );

        if (overlap) {
            return ResponseEntity
                    .badRequest()
                    .body("Leave already exists for selected dates");
        }

        // 📝 Create leave request
        LeaveRequest leave = new LeaveRequest();
        leave.setEmployee(emp);
        leave.setLeaveType(request.getLeaveType());
        leave.setReason(request.getReason());
        leave.setStartDate(request.getStartDate());
        leave.setEndDate(request.getEndDate());

        // 🧾 Defaults
        leave.setStatus(LeaveStatus.PENDING);
        leave.setRequestDate(LocalDate.now());
        leave.setApprovedDate(null);

        // 💾 Save leave first
        LeaveRequest savedLeave = repo.save(leave);

        // 📎 Save proof URLs (Cloudinary / S3 / CDN)
        if (request.getProofUrls() != null && !request.getProofUrls().isEmpty()) {

            for (String url : request.getProofUrls()) {

                LeaveProof proof = new LeaveProof();
                proof.setLeaveRequest(savedLeave);
                proof.setFileUrl(url);                 // ✅ URL stored
                proof.setFileName("leave-proof");      // optional
                proof.setFileType("cloud");             // optional

                savedLeave.addProof(proof);
            }

            // cascade = ALL → this save is enough
            repo.save(savedLeave);
        }

        response.put("message", "Leave applied successfully");
        response.put("leaveId", savedLeave.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    
    public ResponseEntity<?> getAllEmployeesLeaveRequests() {

        List<LeaveRequest> leaves =
                repo.findAllByOrderByRequestDateDesc();

        if (leaves.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        List<Map<String, Object>> response = leaves.stream().map(leave -> {

            Map<String, Object> map = new HashMap<>();

            // 🔑 Leave info
            map.put("leaveId", leave.getId());
            map.put("leaveType", leave.getLeaveType());
            map.put("reason", leave.getReason());
            map.put("requestDate", leave.getRequestDate());
            map.put("startDate", leave.getStartDate());
            map.put("endDate", leave.getEndDate());
            map.put("status", leave.getStatus());
            map.put("approvedDate", leave.getApprovedDate());

            // 👤 Employee info (MINIMAL)
            Employees emp = leave.getEmployee();
            map.put("employeeId", emp.getEmployeeId());
            map.put("employeeName",
                    emp.getFirstName() + " " + emp.getLastName());

            // 📎 Proof URLs only
            List<String> proofUrls = leave.getProofs()
                    .stream()
                    .map(LeaveProof::getFileUrl)
                    .filter(Objects::nonNull)
                    .toList();

            map.put("proofUrls", proofUrls);

            return map;

        }).toList();

        return ResponseEntity.ok(response);
    }

}
