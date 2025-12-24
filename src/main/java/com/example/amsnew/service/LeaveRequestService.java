package com.example.amsnew.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
<<<<<<< Updated upstream
import java.util.ArrayList;
=======
import java.util.HashMap;
>>>>>>> Stashed changes
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.amsnew.dto.LeaveRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.amsnew.model.Employees;
import com.example.amsnew.model.LeaveProof;
import com.example.amsnew.model.LeaveRequest;
import com.example.amsnew.model.LeaveStatus;
import com.example.amsnew.repository.LeaveProofRepository;
import com.example.amsnew.repository.LeaveRequestRepo;
import com.example.amsnew.repository.UserRepository;

import jakarta.transaction.Transactional;



@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepo repo;
    @Autowired
    private UserRepository userrepo;
    
    @Autowired
    private LeaveProofRepository proofrepo;

    public ResponseEntity<?> applyLeave(
            LeaveRequest request,
            MultipartFile[] files
    ) {

        Map<String, Object> response = new HashMap<>();

        // 🔐 1️⃣ Resolve logged-in employee from JWT
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // from JWT "sub"

        Employees emp = userrepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        request.setEmployee(emp);

        // 2️⃣ Date validation
        if (request.getStartDate().isAfter(request.getEndDate())) {
            return ResponseEntity.badRequest().body("Start date cannot be after end date");
        }

        if (request.getStartDate().isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().body("Leave cannot start in the past");
        }

        // 3️⃣ Overlap check
        boolean overlap = repo.existsOverlappingLeave(
                emp.getId(),
                request.getStartDate(),
                request.getEndDate()
        );

        if (overlap) {
            return ResponseEntity.badRequest().body("Leave already exists for selected dates");
        }

        // 4️⃣ Default values
        request.setStatus(LeaveStatus.PENDING);
        request.setRequestDate(LocalDate.now());
        request.setApprovedDate(null);

        // 5️⃣ Save leave first (to get ID)
        LeaveRequest savedLeave = repo.save(request);

        // 6️⃣ Handle file upload (STORE IN DATABASE)
        if (files != null && files.length > 0) {

            for (MultipartFile file : files) {

                if (file.isEmpty()) continue;

                // Validate file type
                if (!isAllowedType(file.getContentType())) {
                    return ResponseEntity.badRequest().body("Invalid file type");
                }

                // Validate file size (5MB example)
                if (file.getSize() > 5 * 1024 * 1024) {
                    return ResponseEntity.badRequest().body("File size exceeds limit");
                }

                try {
                    LeaveProof proof = new LeaveProof(
                            savedLeave,
                            file.getOriginalFilename(),
                            file.getContentType(),
                            file.getBytes() // ⭐ ACTUAL FILE BYTES
                    );

                    // Maintain bidirectional relationship
                    savedLeave.addProof(proof);

                } catch (IOException e) {
                    return ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("File upload failed");
                }
            }

            // Save again to persist proofs (cascade)
            repo.save(savedLeave);
        }

        response.put("message", "Leave applied successfully");
        response.put("data", savedLeave);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    
    
    private boolean isAllowedType(String type) {
        return type.equals("image/jpeg")
            || type.equals("image/png")
            || type.equals("application/pdf")
            || type.equals("application/msword")
            || type.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
            || type.equals("video/mp4");
    }
    
    
    
    

//    public List<LeaveRequest> getAllLeave() {
//        return  repo.findAll();
//    }
public List<LeaveRequestDTO> getAllLeave() {
    return repo.findAll().stream().map(leave -> {
        LeaveRequestDTO dto = new LeaveRequestDTO();
        dto.setId(leave.getId());
        dto.setEmployeeId(leave.getEmployee().getId().toString());
        dto.setEmployeeFirstName(leave.getEmployee().getFirstName()); // getting first name
        dto.setStartDate(leave.getStartDate());
        dto.setEndDate(leave.getEndDate());
        dto.setStatus(leave.getStatus());
        dto.setReason(leave.getReason().name());
        return dto;
    }).toList();
}

<<<<<<< Updated upstream

//    public List<LeaveRequest> getAllLeaveById(int id) {
//        return repo.findByEmployee_Id(id);
//    }
    public List<LeaveRequestDTO> getAllLeaveByEmployeeId(int id) {
        List<LeaveRequest> leaves = repo.findByEmployee_Id(id);
        List<LeaveRequestDTO> dtoList = new ArrayList<>();

        for (LeaveRequest leave : leaves) {
            LeaveRequestDTO dto = new LeaveRequestDTO();
            dto.setId(leave.getId());
            dto.setEmployeeId(leave.getEmployee().getId().toString());
            dto.setEmployeeFirstName(leave.getEmployee().getFirstName());
            dto.setStartDate(leave.getStartDate());
            dto.setEndDate(leave.getEndDate());
            dto.setStatus(leave.getStatus());
            dto.setReason(leave.getReason().toString());
            dtoList.add(dto);
        }

        return dtoList;
=======
   

    @Transactional
    public LeaveRequest approveLeave(Integer id) {

        LeaveRequest leave = repo.findById(id).orElse(null);

        if (leave == null) {
            return null;
        }

        leave.setStatus(LeaveStatus.APPROVED);
        leave.setApprovedDate(LocalDate.now());

        return repo.save(leave);
>>>>>>> Stashed changes
    }


    public LeaveRequest rejectLeave(Integer id) {
        LeaveRequest leave = repo.findById(id).orElseThrow(() -> new RuntimeException("Leave not found"));
        leave.setStatus(LeaveStatus.REJECTED);
        leave.setApprovedDate(LocalDate.now());
        repo.save(leave);
        return leave;
    }


    public LeaveRequest getLeaveById(Integer id) {
        LeaveRequest leave = repo.findById(id).orElseThrow(() -> new RuntimeException("Leave not found"));
        return leave;
    }

<<<<<<< Updated upstream
//    public List<LeaveRequest> getLeaveStatus(String approved) {
//        return  repo.findAllByStatus(approved);
//    }
public List<LeaveRequestDTO> getLeaveStatus(String approved) {
    List<LeaveRequest> leaves = repo.findAllByStatus(approved);

    return leaves.stream().map(leave -> {
        LeaveRequestDTO dto = new LeaveRequestDTO();
        dto.setId(leave.getId());
        dto.setEmployeeId(leave.getEmployee().getId().toString());
        dto.setEmployeeFirstName(leave.getEmployee().getFirstName());
        dto.setStartDate(leave.getStartDate());
        dto.setEndDate(leave.getEndDate());
        dto.setStatus(leave.getStatus());
        dto.setReason(leave.getReason().toString());
        return dto;
    }).toList();
}


    //    public LeaveRequest getLeaveById(int id) {
//        LeaveRequest leave = repo.findById((long) id).orElseThrow(() -> new RuntimeException("Leave not found"));
//        return leave;
//    }
public LeaveRequestDTO getLeaveById(int id) {
    LeaveRequest leave = repo.findById((long) id)
            .orElseThrow(() -> new RuntimeException("Leave not found"));

    LeaveRequestDTO dto = new LeaveRequestDTO();
    dto.setId(leave.getId());
    dto.setEmployeeId(leave.getEmployee().getId().toString()); // employee id
    dto.setEmployeeFirstName(leave.getEmployee().getFirstName());   // employee name
    dto.setStartDate(leave.getStartDate());
    dto.setEndDate(leave.getEndDate());
    dto.setStatus(leave.getStatus());
    dto.setReason(leave.getReason().toString());

    return dto;
}

=======

	
>>>>>>> Stashed changes
}
