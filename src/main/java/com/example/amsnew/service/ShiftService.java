package com.example.amsnew.service;

import java.util.List;

import jakarta.validation.Valid;
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

    public Shift updateShift(int id, @Valid Shift shift) {
        return repo.findById(id).map(existing -> {
            existing.setShiftType(shift.getShiftType());
            existing.setStartTime(shift.getStartTime());
            existing.setEndTime(shift.getEndTime());
            existing.setGraceMinutes(shift.getGraceMinutes());
            return repo.save(existing);
        }).orElse(null);
    }


    public boolean deleteShift(int id) {

            if (!repo.existsById(id)) {
                return false;
            }
            repo.deleteById(id);
            return true;

    }
}
