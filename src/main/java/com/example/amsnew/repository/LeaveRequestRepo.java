package com.example.amsnew.repository;

import com.example.amsnew.model.LeaveRequest;
import com.example.amsnew.model.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRequestRepo extends JpaRepository<LeaveRequest, Integer> {

    // ✅ All leaves of one employee
    List<LeaveRequest> findByEmployeeId(Long employeeId);

    // ✅ All leaves by status (ADMIN)
    List<LeaveRequest> findAllByStatus(LeaveStatus status);

    // ✅ Check overlapping leave
    @Query("""
        SELECT COUNT(l) > 0
        FROM LeaveRequest l
        WHERE l.employee.id = :empId
          AND l.startDate <= :endDate
          AND l.endDate >= :startDate
    """)
    boolean existsOverlappingLeave(
            @Param("empId") Integer empId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
