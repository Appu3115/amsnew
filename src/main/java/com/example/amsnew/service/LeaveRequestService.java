package com.example.amsnew.service;

//import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
//import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.example.amsnew.dto.LeaveRequestDTO;
import com.example.amsnew.model.*;
import com.example.amsnew.repository.DailyAttendanceStatusRepo;
import com.example.amsnew.repository.HolidayRepo;
import com.example.amsnew.repository.LeaveBalanceRepo;
//import com.example.amsnew.repository.LeaveProofRepository;
import com.example.amsnew.repository.LeaveRequestRepo;
import com.example.amsnew.repository.UserRepository;

//import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;

@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepo repo;

    @Autowired
    private UserRepository userrepo;
    
    @Autowired
    private LeaveBalanceRepo  balanceRepo;
    
    @Autowired
    private HolidayRepo holidayRepo;
    
    @Autowired
    private DailyAttendanceStatusRepo dailyStatusRepo;
    
    @Autowired
    private AttendanceService attendanceService;


//    @Autowired
//    private LeaveProofRepository proofrepo;

  
    public ResponseEntity<?> applyLeave(
            String employeeId,
            LeaveRequestDTO request   // 👈 NEW DTO
    ) {

        Map<String, Object> response = new HashMap<>();

        Employees emp = userrepo
                .findByEmployeeId(employeeId)
                .orElseThrow(() ->
                    new RuntimeException("Employee not found")
                );

        if (request.getStartDate().isAfter(request.getEndDate())) {
            return ResponseEntity.badRequest()
                    .body("Invalid date range");
        }

        if (request.getStartDate().isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest()
                    .body("Leave cannot start in past");
        }

        boolean overlap = repo.existsOverlappingLeave(
                emp.getId(),
                request.getStartDate(),
                request.getEndDate()
        );

        if (overlap) {
            return ResponseEntity.badRequest()
                    .body("Overlapping leave exists");
        }

        // 1️⃣ Init balance
        LeaveBalance balance =
                initLeaveBalanceIfNotExists(emp);

        // 2️⃣ Calculate days
        long payableDays =
                calculatePayableLeaveDays(
                        request.getStartDate(),
                        request.getEndDate()
                );

        // 3️⃣ Validate balance
        validateLeaveBalance(
                request.getLeaveType(),
                balance,
                payableDays
        );

        // 4️⃣ Restricted holiday check
        boolean containsRestrictedHoliday =
                holidayRepo.hasRestrictedHolidayBetween(
                        request.getStartDate(),
                        request.getEndDate()
                );

        if (containsRestrictedHoliday) {
            validateRestrictedHoliday(balance);
        }


        // 5️⃣ Save leave
        LeaveRequest leave = new LeaveRequest();
        leave.setEmployee(emp);
        leave.setLeaveType(request.getLeaveType());
        leave.setReason(request.getReason());
        leave.setStartDate(request.getStartDate());
        leave.setEndDate(request.getEndDate());
        leave.setStatus(LeaveStatus.PENDING);
        leave.setRequestDate(LocalDate.now());
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
    
  
    private LeaveBalance initLeaveBalanceIfNotExists(Employees emp) {

        int year = LocalDate.now().getYear();

        Optional<LeaveBalance> optional =
                balanceRepo.findByEmployeeAndYear(emp, year);

        if (optional.isPresent()) {
            return optional.get();
        }

        LeaveBalance balance = new LeaveBalance();
        balance.setEmployee(emp);
        balance.setYear(year);
        balance.setRestrictedLeaveUsed(0);

        if (emp.getStatus() == EmployeeStatus.PROBATION) {
            balance.setCasualLeave(0);
            balance.setSickLeave(12);
            balance.setEarnedLeave(0);
        } else {
            balance.setCasualLeave(12);
            balance.setSickLeave(12);
            balance.setEarnedLeave(12);
        }

        return balanceRepo.save(balance);
    }
    
    //Calculate Leave Days
    private long calculatePayableLeaveDays(
            LocalDate start,
            LocalDate end
    ) {
        long totalDays =
                ChronoUnit.DAYS.between(start, end) + 1;

        long holidayCount =
                holidayRepo.countCompanyHolidays(start, end);

        return totalDays - holidayCount;
    }

    //Validate Leave Balance
    private void validateLeaveBalance(
            LeaveType leaveType,
            LeaveBalance balance,
            long payableDays
    ) {

        switch (leaveType) {

            case CASUAL -> {
                if (balance.getCasualLeave() < payableDays)
                    throw new RuntimeException(
                        "Insufficient Casual Leave balance"
                    );
            }

            case SICK -> {
                if (balance.getSickLeave() < payableDays)
                    throw new RuntimeException(
                        "Insufficient Sick Leave balance"
                    );
            }

            case EARNED -> {
                if (balance.getEarnedLeave() < payableDays)
                    throw new RuntimeException(
                        "Insufficient Earned Leave balance"
                    );
            }
        }
    }


    //Restricted Holiday Rule (Max 2)
    private void validateRestrictedHoliday(
            LeaveBalance balance
    ) {
        if (balance.getRestrictedLeaveUsed() >= 2) {
            throw new RuntimeException(
                "Restricted Holiday limit exceeded (Max 2)"
            );
        }
    }


   //approve leave
    
    public ResponseEntity<?> approveLeave(Integer leaveId) {

        LeaveRequest leave =
                repo.findById(leaveId)
                    .orElseThrow(() -> new RuntimeException("Leave not found"));

        Employees emp = leave.getEmployee();
        LocalDate start = leave.getStartDate();
        LocalDate end = leave.getEndDate();

        LeaveBalance balance =
                balanceRepo.findByEmployeeAndYear(
                        emp, LocalDate.now().getYear()
                ).orElseThrow();

        long days = calculatePayableLeaveDays(start, end);

        // 1️⃣ Deduct leave balance
        switch (leave.getLeaveType()) {
            case CASUAL ->
                balance.setCasualLeave(balance.getCasualLeave() - days);
            case SICK ->
                balance.setSickLeave(balance.getSickLeave() - days);
            case EARNED ->
                balance.setEarnedLeave(balance.getEarnedLeave() - days);
        }

        // 2️⃣ Mark LEAVE in DailyAttendanceStatus (DATE BY DATE)
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {

            final LocalDate leaveDate = date; 

            boolean isHoliday =
                    holidayRepo.existsByDate(leaveDate);

            boolean isWeeklyOff =
                    attendanceService.isWeeklyOff(
                            leaveDate,
                            emp.getShift()
                    );

            if (isHoliday || isWeeklyOff) {
                continue;
            }

            DailyAttendanceStatus daily =
                    dailyStatusRepo
                        .findByEmployeeAndAttendanceDate(emp, leaveDate)
                        .orElseGet(() -> {
                            DailyAttendanceStatus d =
                                    new DailyAttendanceStatus();
                            d.setEmployee(emp);
                            d.setAttendanceDate(leaveDate); // ✅ SAFE
                            return d;
                        });

            daily.setStatus(DailyStatus.LEAVE);
            dailyStatusRepo.save(daily);
        

        }

        // 3️⃣ Restricted holiday handling
        boolean hasRestrictedHoliday =
                holidayRepo.hasRestrictedHolidayBetween(start, end);

        if (hasRestrictedHoliday) {
            balance.setRestrictedLeaveUsed(
                    balance.getRestrictedLeaveUsed() + 1
            );
        }

        // 4️⃣ Final approval
        leave.setStatus(LeaveStatus.APPROVED);
        leave.setApprovedDate(LocalDate.now());

        balanceRepo.save(balance);
        repo.save(leave);

        return ResponseEntity.ok("Leave approved");
    }

    
    //reject leave
    public ResponseEntity<?> rejectLeave(Integer leaveId) {

        LeaveRequest leave = repo.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        leave.setStatus(LeaveStatus.REJECTED);
        leave.setApprovedDate(LocalDate.now());

        repo.save(leave);

        return ResponseEntity.ok("Leave rejected");
    }
    
    //leave balance
    public ResponseEntity<?> getLeaveBalance(String employeeId) {

        Employees emp = userrepo.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LeaveBalance balance = balanceRepo.findByEmployeeAndYear(
                emp, LocalDate.now().getYear()
        ).orElseThrow();

        return ResponseEntity.ok(balance);
    }



}
