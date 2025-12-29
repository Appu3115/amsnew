package com.example.amsnew.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.amsnew.dto.DepartmentListResponse;
import com.example.amsnew.dto.DepartmentRequest;
import com.example.amsnew.model.Department;
import com.example.amsnew.model.Employees;
import com.example.amsnew.repository.DepartmentRepository;



@Service
public class DepartmentService {
     @Autowired
     private DepartmentRepository departmentRepo;
     
     public Department addDepartment(String deptName)
     {
    	 if(departmentRepo.findByDeptName(deptName).isPresent())
    	 {
    		 throw new RuntimeException("Department already exists");
    	 }
    	 
    	 Department dept=new Department();
    	 dept.setDeptName(deptName);
    	 dept.setDepartmentCode("DEPT-"+deptName.toUpperCase().replace(" ",""));
    	 dept.setActive(true);
    	 
    	 return departmentRepo.save(dept);
     }
     
     public List<DepartmentListResponse> getAllDepartmentsFinal() {

    	    return departmentRepo.findAll().stream().map(dept -> {
    	        DepartmentListResponse dto = new DepartmentListResponse();
    	        dto.setId(dept.getId());
    	        dto.setDeptName(dept.getDeptName());
    	        dto.setDepartmentCode(dept.getDepartmentCode());
    	        dto.setActive(dept.isActive());
    	        dto.setEmployeeCount(
    	            dept.getEmployees() == null ? 0 : dept.getEmployees().size()
    	        );
    	        List<Employees> employees = dept.getEmployees();
    	        dto.setEmployees(employees);
    	        return dto;
    	    }).toList();
    	}

     
     public List<Department> getAllActiveDepartments()
     {
    	 return departmentRepo.findByActiveTrue();
     }
     
     public Department getDepartmentById(Long id)
     {
    	 return departmentRepo.findById(id).orElseThrow(()-> new RuntimeException("Department not found"));
     }
     
     public String disableDepartment(Long id)
     {
    	 Department dept=departmentRepo.findById(id).orElseThrow(()-> new RuntimeException("Department not found"));
    	 
    	 if(!dept.isActive())
    	 {
    		 return "Department already disabled";
    	 }
    	 
    	 dept.setActive(false);
    	 departmentRepo.save(dept);
    	 
    	 return "Department disabled successfully";
     }
     public Department updateDepartment(DepartmentRequest newDept)
     {
    	 Department oldDept=departmentRepo.findById(newDept.getId()).orElseThrow(()-> new RuntimeException("Department not found"));
    	 
    	 if(!oldDept.isActive())
    	 {
    		 throw new RuntimeException("Cannot update inactive department");
    	 }
    	 
    	 oldDept.setDeptName(newDept.getDeptName());
    	 
    	 String newCode=generateDepartmentCode(newDept.getDeptName());
    	 oldDept.setDepartmentCode(newCode);
    	 
    	 return departmentRepo.save(oldDept);
     }
     private String generateDepartmentCode(String deptName)
     {
    	 return "DEPT-"+deptName.toUpperCase().replace(" ", "_");
     }
}
