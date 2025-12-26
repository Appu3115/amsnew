package com.example.amsnew.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.amsnew.dto.AssignShiftRequest;
import com.example.amsnew.model.EmployeeShifts;
import com.example.amsnew.model.Employees;
import com.example.amsnew.model.Shift;
import com.example.amsnew.repository.EmployeeShiftsRepository;
import com.example.amsnew.repository.ShiftRepo;
import com.example.amsnew.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class EmployeeShiftsService {

	@Autowired
	private EmployeeShiftsRepository employeeshiftsrepo;
	
	@Autowired
	private UserRepository userrepo;
	
	@Autowired
	private ShiftRepo shiftRepo;
	
	@Transactional
	public ResponseEntity<?> assignShift(AssignShiftRequest request) {

	    Optional<Employees> employeeOpt =
	            userrepo.findByEmployeeId(request.getEmployeeId());

	    if (employeeOpt.isEmpty()) {
	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)
	                .body(Map.of("message", "Employee not found"));
	    }

	    Optional<Shift> shiftOpt =
	            shiftRepo.findById(request.getShiftId());

	    if (shiftOpt.isEmpty()) {
	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)
	                .body(Map.of("message", "Shift not found"));
	    }

	    // ❗ Prevent assigning if already has active shift
	    if (employeeshiftsrepo
	            .findByEmployeeIdAndEndDateIsNull(request.getEmployeeId())
	            .isPresent()) {

	        return ResponseEntity
	                .status(HttpStatus.CONFLICT)
	                .body(Map.of(
	                        "message",
	                        "Employee already has an active shift. Use updateShift."
	                ));
	    }

	    Employees employee = employeeOpt.get();
	    Shift shift = shiftOpt.get();

	    EmployeeShifts history = new EmployeeShifts();
	    history.setEmployeeId(employee.getEmployeeId());
	    history.setShift(shift);
	    history.setStartDate(request.getStartDate());
	    history.setEndDate(null);

	    employeeshiftsrepo.save(history);

	    employee.setShift(shift);
	    employee.setUpdatedAt(LocalDateTime.now());

	    userrepo.save(employee);

	    return ResponseEntity
	            .status(HttpStatus.CREATED)
	            .body(Map.of("message", "Shift assigned successfully"));
	}


	
	@Transactional
	public ResponseEntity<?> updateShift(AssignShiftRequest request) {

	    Optional<Employees> employeeOpt =
	            userrepo.findByEmployeeId(request.getEmployeeId());

	    if (employeeOpt.isEmpty()) {
	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)
	                .body(Map.of("message", "Employee not found"));
	    }

	    Optional<Shift> shiftOpt =
	            shiftRepo.findById(request.getShiftId());

	    if (shiftOpt.isEmpty()) {
	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)
	                .body(Map.of("message", "Shift not found"));
	    }

	    Employees employee = employeeOpt.get();
	    Shift newShift = shiftOpt.get();
	    LocalDate newStartDate = request.getStartDate();

	    Optional<EmployeeShifts> activeShiftOpt =
	            employeeshiftsrepo
	                    .findByEmployeeIdAndEndDateIsNull(employee.getEmployeeId());

	    if (activeShiftOpt.isEmpty()) {
	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .body(Map.of(
	                        "message",
	                        "No active shift found. Use assignShift first."
	                ));
	    }

	    EmployeeShifts activeShift = activeShiftOpt.get();

	    if (activeShift.getShift().getId().equals(newShift.getId())) {
	        return ResponseEntity
	                .status(HttpStatus.CONFLICT)
	                .body(Map.of("message", "Employee is already on this shift"));
	    }

	    // Close previous shift
	    activeShift.setEndDate(newStartDate.minusDays(1));
	    employeeshiftsrepo.save(activeShift);

	    // New shift history
	    EmployeeShifts newHistory = new EmployeeShifts();
	    newHistory.setEmployeeId(employee.getEmployeeId());
	    newHistory.setShift(newShift);
	    newHistory.setStartDate(newStartDate);
	    newHistory.setEndDate(null);

	    employeeshiftsrepo.save(newHistory);

	    employee.setShift(newShift);
	    employee.setUpdatedAt(LocalDateTime.now());
	    userrepo.save(employee);

	    return ResponseEntity
	            .ok(Map.of("message", "Shift updated successfully"));
	}



	
	
}
