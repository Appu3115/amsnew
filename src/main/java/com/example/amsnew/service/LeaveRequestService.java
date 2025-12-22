package com.example.amsnew.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.amsnew.dto.LeaveRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.amsnew.model.Employees;
import com.example.amsnew.model.LeaveRequest;
import com.example.amsnew.repository.LeaveRequestRepo;
import com.example.amsnew.repository.UserRepository;



@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepo repo;
    @Autowired
    private UserRepository userrepo;

    public LeaveRequest applyLeave(LeaveRequest request) {
        Employees emp = userrepo.findById(request.getEmployee().getId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        request.setEmployee(emp);

        if (request.getStatus() == null) {
            request.setStatus("pending");
        }
        request.setRequestDate(LocalDate.now());
        return repo.save(request);
    }


//    public List<LeaveRequest> getAllLeave() {
//        return  repo.findAll();
//    }
public List<LeaveRequestDTO> getAllLeave() {
    return repo.findAll().stream().map(leave -> {
        LeaveRequestDTO dto = new LeaveRequestDTO();
        dto.setId(leave.getId());
        dto.setEmployeeId(leave.getEmployee().getId().toString());
        dto.setEmployeeFirstName(leave.getEmployee().getFirstName()); // getting first name
        dto.setStartDate(leave.getStartDate());
        dto.setEndDate(leave.getEndDate());
        dto.setStatus(leave.getStatus());
        dto.setReason(leave.getReason().name());
        return dto;
    }).toList();
}


//    public List<LeaveRequest> getAllLeaveById(int id) {
//        return repo.findByEmployee_Id(id);
//    }
    public List<LeaveRequestDTO> getAllLeaveByEmployeeId(int id) {
        List<LeaveRequest> leaves = repo.findByEmployee_Id(id);
        List<LeaveRequestDTO> dtoList = new ArrayList<>();

        for (LeaveRequest leave : leaves) {
            LeaveRequestDTO dto = new LeaveRequestDTO();
            dto.setId(leave.getId());
            dto.setEmployeeId(leave.getEmployee().getId().toString());
            dto.setEmployeeFirstName(leave.getEmployee().getFirstName());
            dto.setStartDate(leave.getStartDate());
            dto.setEndDate(leave.getEndDate());
            dto.setStatus(leave.getStatus());
            dto.setReason(leave.getReason().toString());
            dtoList.add(dto);
        }

        return dtoList;
    }

    public LeaveRequest approveLeave(int id) {
        LeaveRequest leave = repo.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));
        leave.setStatus("approved");
        leave.setApprovedDate(LocalDate.now());
        repo.save(leave);
        return leave;
    }

    public LeaveRequest rejectLeave(int id) {
        LeaveRequest leave = repo.findById((long) id).orElseThrow(() -> new RuntimeException("Leave not found"));
        leave.setStatus("rejected");
        leave.setApprovedDate(LocalDate.now());
        repo.save(leave);
        return leave;
    }

//    public List<LeaveRequest> getLeaveStatus(String approved) {
//        return  repo.findAllByStatus(approved);
//    }
public List<LeaveRequestDTO> getLeaveStatus(String approved) {
    List<LeaveRequest> leaves = repo.findAllByStatus(approved);

    return leaves.stream().map(leave -> {
        LeaveRequestDTO dto = new LeaveRequestDTO();
        dto.setId(leave.getId());
        dto.setEmployeeId(leave.getEmployee().getId().toString());
        dto.setEmployeeFirstName(leave.getEmployee().getFirstName());
        dto.setStartDate(leave.getStartDate());
        dto.setEndDate(leave.getEndDate());
        dto.setStatus(leave.getStatus());
        dto.setReason(leave.getReason().toString());
        return dto;
    }).toList();
}


    //    public LeaveRequest getLeaveById(int id) {
//        LeaveRequest leave = repo.findById((long) id).orElseThrow(() -> new RuntimeException("Leave not found"));
//        return leave;
//    }
public LeaveRequestDTO getLeaveById(int id) {
    LeaveRequest leave = repo.findById((long) id)
            .orElseThrow(() -> new RuntimeException("Leave not found"));

    LeaveRequestDTO dto = new LeaveRequestDTO();
    dto.setId(leave.getId());
    dto.setEmployeeId(leave.getEmployee().getId().toString()); // employee id
    dto.setEmployeeFirstName(leave.getEmployee().getFirstName());   // employee name
    dto.setStartDate(leave.getStartDate());
    dto.setEndDate(leave.getEndDate());
    dto.setStatus(leave.getStatus());
    dto.setReason(leave.getReason().toString());

    return dto;
}

}
