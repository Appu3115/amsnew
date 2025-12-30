package com.example.amsnew.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.amsnew.model.SessionType;
import com.example.amsnew.model.WorkSession;

public interface WorkSessionRepository
        extends JpaRepository<WorkSession, Integer> {

    Optional<WorkSession> findByEmployee_EmployeeIdAndEndTimeIsNull(String employeeId);
    
    @Query("""
            SELECT w FROM WorkSession w
            WHERE w.employee.employeeId = :employeeId
            AND w.sessionType = 'WORK'
            AND w.startTime BETWEEN :start AND :end
        """)
        List<WorkSession> findWorkSessions(
                String employeeId,
                LocalDateTime start,
                LocalDateTime end
        );
    
    
    List<WorkSession> findByEmployee_EmployeeIdAndSessionTypeInAndStartTimeBetween(
            String employeeId,
            List<SessionType> sessionTypes,
            LocalDateTime start,
            LocalDateTime end
    );

}
