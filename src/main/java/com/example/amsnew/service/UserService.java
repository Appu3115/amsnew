package com.example.amsnew.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.amsnew.config.JwtUtil;
import com.example.amsnew.dto.LoginRequest;
import com.example.amsnew.dto.RegisterDTO;
import com.example.amsnew.model.Department;
import com.example.amsnew.model.Employees;
import com.example.amsnew.repository.DepartmentRepository;
import com.example.amsnew.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

	@Autowired
	private UserRepository userrepo;
	
	@Autowired
	private DepartmentRepository departmentrepo;
	
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	
	@Transactional
	public ResponseEntity<?> createEmployee(RegisterDTO request) {

	    if (request == null) {
	        return ResponseEntity.badRequest().body("Request body is missing");
	    }

	    // employeeId
	    String empId = trimOrNull(request.getEmployeeId());
	    if (empId == null || empId.isEmpty()) {
	        return ResponseEntity.badRequest().body("employeeId is required");
	    }
	    if (userrepo.existsByEmployeeId(empId)) {
	        return ResponseEntity.badRequest().body("employeeId already exists");
	    }

	    // firstName
	    String firstName = trimOrNull(request.getFirstName());
	    if (firstName == null || firstName.isEmpty()) {
	        return ResponseEntity.badRequest().body("firstName is required");
	    }

	    // lastName
	    String lastName = trimOrNull(request.getLastName());
	    if (lastName == null || lastName.isEmpty()) {
	        return ResponseEntity.badRequest().body("lastName is required");
	    }

	    // email
	    String email = trimOrNull(request.getEmail());
	    if (email == null || email.isEmpty()) {
	        return ResponseEntity.badRequest().body("email is required");
	    }
	    if (!isValidEmail(email)) {
	        return ResponseEntity.badRequest().body("Invalid email format");
	    }
	    if (userrepo.existsByEmail(email)) {
	        return ResponseEntity.badRequest().body("Email already exists");
	    }

	    // password
	    if (request.getPassword() == null || request.getConfirmPassword() == null) {
	        return ResponseEntity.badRequest().body("Password and confirmPassword are required");
	    }
	    if (!request.getPassword().equals(request.getConfirmPassword())) {
	        return ResponseEntity.badRequest().body("Passwords do not match");
	    }

	    // phone
	    String phone = trimOrNull(request.getPhone());
	    if (phone == null || !phone.matches("\\d{10}")) {
	        return ResponseEntity.badRequest().body("phone must be exactly 10 digits");
	    }

	    

	    Department department = departmentrepo.findByDeptNameIgnoreCase(request.getDepartmentName())
	            .orElse(null);

	    if (department == null) {
	        return ResponseEntity.badRequest().body("Invalid department");
	    }

	    // role
	    String role = trimOrNull(request.getRole());
	    if (role == null || role.isEmpty()) {
	        return ResponseEntity.badRequest().body("role is required");
	    }

	    // joinDate
	    LocalDate joinDate;
	    try {
	        joinDate = LocalDate.parse(request.getJoinDate());
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body("Invalid joinDate format (yyyy-MM-dd)");
	    }

	    // save employee
	    Employees emp = new Employees();
	    emp.setEmployeeId(empId);
	    emp.setFirstName(firstName);
	    emp.setLastName(lastName);
	    emp.setEmail(email);
	    emp.setPassword(passwordEncoder.encode(request.getPassword()));
	    emp.setPhone(phone);
	    emp.setDepartment(department);
	    emp.setRole(role);
	    emp.setJoinDate(joinDate);
	    emp.setActive(true);
	    emp.setCreatedAt(LocalDateTime.now());
	    emp.setUpdatedAt(LocalDateTime.now());

	    userrepo.save(emp);

	    return ResponseEntity
	            .status(HttpStatus.CREATED)
	            .body("Employee registered successfully");
	}


	 
	public ResponseEntity<?> loginEmployee(LoginRequest request) {

	    if (request == null) {
	        return ResponseEntity.badRequest().body("Request body is missing");
	    }


	    String email = trimOrNull(request.getEmail());
	    if (email == null || email.isEmpty()) {
	        return ResponseEntity.badRequest().body("Email is required");
	    }
	    if (!isValidEmail(email)) {
	        return ResponseEntity.badRequest().body("Invalid email format");
	    }

	    Optional<Employees> optEmp = userrepo.findByEmail(email);
	    if (optEmp.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body("Invalid email or password");
	    }

	    Employees emp = optEmp.get();

	   
	    if (!emp.isActive()) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                .body("Account is deactivated. Contact admin.");
	    }

	   
	    String employeeId = trimOrNull(request.getEmployeeId());
	    if (employeeId == null || !employeeId.equals(emp.getEmployeeId())) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body("Invalid employeeId");
	    }

	    
	    if (request.getPassword() == null ||
	        !passwordEncoder.matches(request.getPassword(), emp.getPassword())) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body("Invalid email or password");
	    }

	    
	    String token = jwtUtil.generateToken(emp.getEmail());

	    
	    emp.setLastLoginAt(LocalDateTime.now());
	    userrepo.save(emp);

	    
	    Map<String, Object> response = new HashMap<>();
	    response.put("employeeId", emp.getEmployeeId());
	    response.put("email", emp.getEmail());
	    response.put("firstName", emp.getFirstName());
	    response.put("lastName", emp.getLastName());
	    response.put("role", emp.getRole());
	    response.put("token", token);
	    response.put("tokenType", "Bearer");
	    response.put("message", "Login successful");

	    HttpHeaders headers = new HttpHeaders();
	    headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);

	    return ResponseEntity.ok()
	            .headers(headers)
	            .body(response);
	}

	 
	 public ResponseEntity<?> getAllEmployees(){
		 List<Employees> li = userrepo.findAll();
		  if(li.isEmpty()) {
			  return ResponseEntity.badRequest().body("No Employees Found");
		  }
		 return ResponseEntity.ok(li);
	 }
	 
	 @Transactional
	 public ResponseEntity<?> deleteEmployee(String employeeId) {

		    Optional<Employees> empopt = userrepo.findByEmployeeId(employeeId);

		    if (empopt.isEmpty()) {
		        return ResponseEntity.badRequest().body("No Employee Found");
		    }

		    userrepo.deleteByEmployeeId(employeeId);
		    return ResponseEntity.ok("Employee Deleted Successfully");
		}

	 public ResponseEntity<?> deactivateEmployee(String employeeId) {
		    Optional<Employees> emp = userrepo.findByEmployeeId(employeeId);

		    if (emp.isEmpty()) {
		        return ResponseEntity.badRequest().body("Employee not found");
		    }

		    Employees employee = emp.get();
		    employee.setActive(false);
		    userrepo.save(employee);

		    return ResponseEntity.ok("Employee deactivated successfully");
		}

	 private boolean isValidEmail(String email) {
	        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
	        return email != null && email.matches(emailRegex);
	    }
	 
	 private String trimOrNull(String s) {
	        return s == null ? null : s.trim();
	    }
}
