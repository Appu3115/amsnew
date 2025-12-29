package com.example.amsnew.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.amsnew.model.EmployeeShifts;
//import com.example.amsnew.model.Employees;

@Repository
public interface EmployeeShiftsRepository extends JpaRepository<EmployeeShifts, Integer> {

	List<EmployeeShifts> findByEmployeeId(String empId);

	 Optional<EmployeeShifts> findByEmployeeIdAndEndDateIsNull(String employeeId);

}
