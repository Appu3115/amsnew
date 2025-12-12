package com.example.amsnew.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.amsnew.model.EmployeeShifts;

@Repository
public interface EmployeeShiftsRepository extends JpaRepository<EmployeeShifts, Integer> {

	List<EmployeeShifts> findByEmployeeId(String empId);

}
