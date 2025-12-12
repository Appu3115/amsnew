package com.example.amsnew.service;

import java.time.LocalDate;
import java.util.List;

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
        return repo.save(request);
    }


    public List<LeaveRequest> getAllLeave() {
        return  repo.findAll();
    }

    public List<LeaveRequest> getAllLeaveById(int id) {
        return repo.findByEmployee_Id(id);
    }

    public LeaveRequest approveLeave(int id) {
        LeaveRequest leave = repo.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));
        leave.setStatus("approved");
        leave.setApprovedDate(LocalDate.now());
        return repo.save(leave);
    }

    public LeaveRequest rejectLeave(int id) {
        LeaveRequest leave = repo.findById((long) id).orElseThrow(() -> new RuntimeException("Leave not found"));
        leave.setStatus("rejected");
        leave.setApprovedDate(LocalDate.now());
        repo.save(leave);
        return leave;
    }

    public List<LeaveRequest> getLeaveStatus(String approved) {
        return  repo.findAllByStatus(approved);
    }
}
