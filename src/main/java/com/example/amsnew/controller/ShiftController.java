package com.example.amsnew.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.amsnew.model.Shift;
import com.example.amsnew.service.ShiftService;



@RestController
@RequestMapping("/shift")
public class ShiftController {

    @Autowired
    private ShiftService service;

    @GetMapping("/getShift")
    public List<Shift> getAllShifts(){
        return service.getAllShifts();
    }

    @PostMapping("/addShift")
    public String addShift(@RequestBody Shift shift){
        service.addShift(shift);
        return "Added Successfully";
    }

    @GetMapping("/getDayShift")
    public List<Shift> getAllDayShifts(){
        return service.getShiftsByType("Day");
    }

    @GetMapping("/getNightShift")
    public List<Shift> getAllNightShifts(){
        return service.getShiftsByType("Night");
    }
    @GetMapping("/getEveningShifts")
    public List<Shift> getEveningShifts(){
        return service.getShiftsByType("Evening");
    }

}
