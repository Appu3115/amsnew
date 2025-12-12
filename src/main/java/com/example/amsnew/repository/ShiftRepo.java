package com.example.amsnew.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.amsnew.model.Shift;



@Repository
public interface ShiftRepo extends JpaRepository<Shift, Integer> {
    List<Shift> findAllByShiftType(String day);
}
