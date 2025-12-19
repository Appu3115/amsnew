package com.example.amsnew.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;   
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.amsnew.dto.DepartmentListResponse;
import com.example.amsnew.dto.DepartmentRequest;
import com.example.amsnew.model.Department;
import com.example.amsnew.service.DepartmentService;


@RestController
@RequestMapping("/department")
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;
	
	@PostMapping("/add")
	public Department addDepartment(@RequestBody DepartmentRequest request)
	{
		
		return departmentService.addDepartment(request.getDeptName());
	}
	
	@GetMapping("fetchAll")
	public List<DepartmentListResponse> getAllDepartments() {
	    return departmentService.getAllDepartmentsFinal();
	}

	
	@GetMapping("fetchActiveDept")
	public List<Department> getAllActiveDepartments()
	{
		return departmentService.getAllActiveDepartments();
	}
	
	
	@GetMapping("/getDept/{id}")
	public Department getDepartment(@PathVariable Long id)
	{
		return departmentService.getDepartmentById(id);
	}
	
	@DeleteMapping("/disable/{id}")
	public String disableDepartment(@PathVariable Long id)
	{
		return departmentService.disableDepartment(id);
	}
	
	@PutMapping("/update/{id}")
	public Department updateDepartment(@RequestBody DepartmentRequest request)
	{
		return departmentService.updateDepartment(request);
	}
}
