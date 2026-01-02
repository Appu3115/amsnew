package com.example.amsnew.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.amsnew.dto.HolidayDTO;
import com.example.amsnew.model.Holiday;
import com.example.amsnew.repository.HolidayRepo;

@Service
public class HolidayService {

    @Autowired
    private HolidayRepo holidayRepo;

    // Add single holiday
    public ResponseEntity<?> addHoliday(HolidayDTO dto) {
    	


        // 1. Validate date exists
        if (dto.getDate() == null || dto.getDate().isBlank()) {
            return ResponseEntity.badRequest().body("Date is required");
        }

        // 2. Convert String → LocalDate (SAFE WAY)
        LocalDate holidayDate;
        try {
            holidayDate = LocalDate.parse(dto.getDate().trim());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Invalid date format. Use yyyy-MM-dd");
        }

        // 3. Save holiday
        Holiday holiday = new Holiday();
        holiday.setTitle(dto.getTitle());
        holiday.setDate(holidayDate);
        holiday.setType(dto.getType());

        holidayRepo.save(holiday);

        return ResponseEntity.ok("Holiday added successfully");
    }



    // Get holidays by year (TABLE FORMAT)
    public ResponseEntity<?> getHolidaysByYear(int year) {

        List<Holiday> holidays =
                holidayRepo.findByYear(year);

        List<Map<String, Object>> response = holidays.stream().map(h -> {

            Map<String, Object> map = new HashMap<>();
            map.put("title", h.getTitle());
            map.put("date", h.getDate());
            map.put("day", h.getDate().getDayOfWeek().toString());
            map.put("type", h.getType());

            return map;
        }).toList();

        return ResponseEntity.ok(response);
    }
}
