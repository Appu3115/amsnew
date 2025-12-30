	package com.example.amsnew.controller;

import java.util.Collections;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.amsnew.model.Shift;
import com.example.amsnew.service.ShiftService;



@RestController
@RequestMapping("/shift")
public class ShiftController {

    @Autowired
    private ShiftService service;

    @GetMapping("/getAllShift")
    public ResponseEntity<List<Shift>> getAllShifts(){
        List<Shift> shifts = service.getAllShifts();
        if (shifts.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(shifts);
    }

    @GetMapping("/getShift/{id}")
    public ResponseEntity<?> getShiftById(@PathVariable int id){
        Shift shift = service.getShiftById(id);
        if(shift != null){
            return ResponseEntity.ok(shift);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Shift with id " + id + " not found");
        }

    }

    @PostMapping("/addShift")
    public ResponseEntity<Shift > addShift(@Valid @RequestBody Shift shift){
        Shift savedShift = service.addShift(shift);
        System.out.println(savedShift);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedShift);
    }

    @PutMapping("/updateShift/{id}")
    public ResponseEntity<?> updateShift(
            @PathVariable int id,
            @Valid @RequestBody Shift shift) {

        Shift updatedShift = service.updateShift(id, shift);

        if (updatedShift == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Update failed. Shift not found.");
        }

        return ResponseEntity.ok(updatedShift);
    }

    @DeleteMapping("/deleteShift/{id}")
    public ResponseEntity<?> deleteShift(@PathVariable int id) {

        boolean deleted = service.deleteShift(id);

        if (!deleted) {
            return ResponseEntity.badRequest().body("Delete failed. Shift not found.");
        }

        return ResponseEntity.ok("Shift deleted successfully");
    }


    @GetMapping("/getDayShifts")
    public ResponseEntity<List<Shift>> getAllDayShifts(){
        List<Shift> shift = service.getShiftsByType("Day");
        if(shift.isEmpty()){
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(shift);
    }

    @GetMapping("/getNightShifts")
    public ResponseEntity<List<Shift>> getAllNightShifts(){
        List<Shift> shift =  service.getShiftsByType("Night");
        if(shift.isEmpty()){
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(shift);
    }
    @GetMapping("/getEveningShifts")
    public ResponseEntity<List<Shift>> getEveningShifts(){
        List<Shift> shift = service.getShiftsByType("Evening");
        if(shift.isEmpty()){
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(shift);
    }

}
