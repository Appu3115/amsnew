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
		System.out.print("DepTName:"+request.getDeptName());
		Department dept=new Department();
		dept.setDeptName(request.getDeptName());
		
		return departmentService.saveDepartment(dept);
	}
	
	@GetMapping("/list")
	public List<Department> getDepartments()
	{
		return departmentService.getAllDepartments();
	}
	
	@GetMapping("/{id}")
	public Department getDepartment(@PathVariable int id)
	{
		return departmentService.getDepartmentById(id);
	}
	
	@DeleteMapping("/delete/{id}")
	public String deleteDepartment(@PathVariable int id)
	{
		return departmentService.deleteDepartment(id);
	}
	
	@PutMapping("/update/{id}")
	public Department updateDepartment(@PathVariable int id,@RequestBody Department dept)
	{
		return departmentService.updateDepartment(id,dept);
	}
}
