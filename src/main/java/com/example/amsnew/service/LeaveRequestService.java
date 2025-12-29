package com.example.amsnew.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            LeaveRequestDTO request,
            MultipartFile[] files
    ) {

        Map<String, Object> response = new HashMap<>();

        // 👤 Get employee DIRECTLY from request
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

        // 💾 Save leave
        LeaveRequest savedLeave = repo.save(leave);

        // 📎 File upload
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {

                if (file.isEmpty()) continue;
                
                String contentType = file.getContentType();
                if (contentType == null || !isAllowedType(contentType)) {
                    return ResponseEntity
                            .badRequest()
                            .body("Unsupported file type: " + contentType);
                }

                if (file.getSize() > 5 * 1024 * 1024) {
                    return ResponseEntity
                            .badRequest()
                            .body("File size exceeds limit");
                }

                try {
                    LeaveProof proof = new LeaveProof();
                    proof.setLeaveRequest(savedLeave);
                    proof.setFileName(file.getOriginalFilename());
                    proof.setFileType(file.getContentType());
                    proof.setFileData(file.getBytes());

                    savedLeave.addProof(proof);

                } catch (IOException e) {
                    return ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("File upload failed");
                }
            }
            repo.save(savedLeave);
        }

        response.put("message", "Leave applied successfully");
        response.put("leaveId", savedLeave.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }



    /* ================= FILE TYPE VALIDATION ================= */
    private boolean isAllowedType(String type) {
        return type.equals("image/jpeg")
                || type.equals("image/png")
                || type.equals("application/pdf")
                || type.equals("application/msword")
                || type.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                || type.equals("video/mp4");
    }

}
