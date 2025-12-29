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

        // ===============================
        // DTO ➜ ENTITY
        // ===============================
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

//    /* ================= GET ALL LEAVES ================= */
//    public List<LeaveRequestDTO> getAllLeave() {
//        return repo.findAll().stream().map(this::toDTO).toList();
//    }

    /* ================= GET BY EMPLOYEE ================= */
//    public List<LeaveRequestDTO> getAllLeaveByEmployeeId(Integer id) {
//        List<LeaveRequest> leaves = repo.findByEmployeeId(id);
//        List<LeaveRequestDTO> dtoList = new ArrayList<>();
//
//        for (LeaveRequest leave : leaves) {
//            dtoList.add(toDTO(leave));
//        }
//        return dtoList;
//    }

    /* ================= APPROVE LEAVE ================= */
    @Transactional
    public LeaveRequest approveLeave(Integer id) {
        LeaveRequest leave = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        leave.setStatus(LeaveStatus.APPROVED);
        leave.setApprovedDate(LocalDate.now());

        return repo.save(leave);
    }

    /* ================= REJECT LEAVE ================= */
    public LeaveRequest rejectLeave(Integer id) {
        LeaveRequest leave = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        leave.setStatus(LeaveStatus.REJECTED);
        leave.setApprovedDate(LocalDate.now());

        return repo.save(leave);
    }

    /* ================= GET BY ID ================= */
//    public LeaveRequestDTO getLeaveById( Integer id) {
//        LeaveRequest leave = repo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Leave not found"));
//
//        return toDTO(leave);
//    }
//
//    /* ================= GET BY STATUS ================= */
//    public List<LeaveRequestDTO> getLeaveStatus(LeaveStatus status) {
//        List<LeaveRequest> leaves = repo.findAllByStatus(status);
//        return leaves.stream().map(this::toDTO).toList();
//    }
//
//    /* ================= DTO MAPPER ================= */
////    private LeaveRequestDTO toDTO(LeaveRequest leave) {
////        LeaveRequestDTO dto = new LeaveRequestDTO();
////        dto.setId(leave.getId());
////        dto.setEmployeeId(leave.getEmployee().getId().toString());
//        dto.setEmployeeFirstName(leave.getEmployee().getFirstName());
//        dto.setStartDate(leave.getStartDate());
//        dto.setEndDate(leave.getEndDate());
//        dto.setStatus(leave.getStatus());
//        dto.setReason(leave.getReason().toString());
//        return dto;
//    }
}
