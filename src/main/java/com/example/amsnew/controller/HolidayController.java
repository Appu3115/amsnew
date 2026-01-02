package com.example.amsnew.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.amsnew.dto.HolidayDTO;

import com.example.amsnew.service.HolidayService;

import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/admin/holidays")
@CrossOrigin("*")
public class HolidayController {

    @Autowired
    private HolidayService holidayService;

    @PostMapping(
        value = "/add",
        consumes = "application/json",
        produces = "application/json"
    )
    public ResponseEntity<?> addHoliday(
            @RequestBody HolidayDTO dto
    ) {
        return holidayService.addHoliday(dto);
    }
    
    
    // Get holidays by year
    @GetMapping("/{year}")
    public ResponseEntity<?> getHolidays(
            @PathVariable int year
    ) {
        return holidayService.getHolidaysByYear(year);
    }
}




