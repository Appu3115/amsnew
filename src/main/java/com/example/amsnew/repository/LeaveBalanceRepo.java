package com.example.amsnew.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.amsnew.model.Employees;
import com.example.amsnew.model.LeaveBalance;

@Repository
public interface LeaveBalanceRepo
        extends JpaRepository<LeaveBalance, Integer> {

    Optional<LeaveBalance> findByEmployeeAndYear(
            Employees employee,
            int year
    );
}

