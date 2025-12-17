package com.example.amsnew.repository;

<<<<<<< HEAD
import java.util.List;
=======
>>>>>>> 2d749b33a0991e9b0769fc283d6c67f26e0cb9c9
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.amsnew.model.Department;


@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer>{
    Optional<Department> findByDeptName(String deptName);
	
	Optional<Department> findByDepartmentCode(String departmentCode);
	
	List<Department> findByActiveTrue();
	
	List<Department> findByActiveFalse();

	
<<<<<<< HEAD
=======
	Optional<Department> findByDeptNameIgnoreCase(String deptName);

    boolean existsByDeptNameIgnoreCase(String deptName);
>>>>>>> 2d749b33a0991e9b0769fc283d6c67f26e0cb9c9
}