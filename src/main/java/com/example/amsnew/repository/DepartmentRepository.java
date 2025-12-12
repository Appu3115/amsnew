package com.example.amsnew.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.amsnew.model.Department;


@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer>{

}