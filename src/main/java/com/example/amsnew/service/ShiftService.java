package com.example.amsnew.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.amsnew.model.Shift;
import com.example.amsnew.repository.ShiftRepo;



@Service
public class ShiftService {
    @Autowired
    private ShiftRepo repo;

    public List<Shift> getAllShifts() {
        return repo.findAll();
    }

    public Shift addShift(Shift shift) {
       return repo.save(shift);
    }

    public List<Shift> getShiftsByType(String day) {
        return repo.findAllByShiftType(day);
    }

    public Shift getShiftById(int id) {
        return repo.findById(id).orElse(null);
    }
}
