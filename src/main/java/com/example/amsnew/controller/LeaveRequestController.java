package com.example.amsnew.controller;


import com.example.amsnew.dto.LeaveRequestDTO;
import com.example.amsnew.service.LeaveRequestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/leave")
@CrossOrigin(origins = "*")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService service;


    @PostMapping(
            value = "/applyleave",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> applyLeave(@RequestParam String employeeId,
            @RequestPart("leave") LeaveRequestDTO request,
            @RequestPart(value = "files", required = false) MultipartFile[] files
    ) {
        return service.applyLeave(employeeId,request, files);
    }

}
