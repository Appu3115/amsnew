package com.example.amsnew.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.amsnew.model.Department;
import com.example.amsnew.repository.DepartmentRepository;



@Service
public class DepartmentService {
     @Autowired
     private DepartmentRepository departmentRepo;
     
     public Department saveDepartment(Department dept)
     {
    	 return departmentRepo.save(dept);
     }
     
     public List<Department> getAllDepartments()
     {
    	 return departmentRepo.findAll();
     }
     public Department getDepartmentById(int id)
     {
    	 return departmentRepo.findById(id).orElse(null);
     }
     
     public String deleteDepartment(int id)
     {
    	 departmentRepo.deleteById(id);
    	 return "Department deleted successfully";
     }
     public Department updateDepartment(int id,Department newDept)
     {
    	 Department oldDept=departmentRepo.findById(id).orElse(null);
    	 
    	 if(oldDept != null)
    	 {
    		 oldDept.setDept_name(newDept.getDept_name());
    		 return departmentRepo.save(oldDept);
    	 }
    	 return null;
     }
}
