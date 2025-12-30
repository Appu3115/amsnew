package com.example.amsnew.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import com.example.amsnew.model.ActivityPulse;

public interface ActivityPulseRepository
        extends JpaRepository<ActivityPulse, Long> {

    @Query("""
        SELECT a FROM ActivityPulse a
        WHERE a.employeeId = :employeeId
        AND a.pulseTime BETWEEN :start AND :end
        ORDER BY a.pulseTime
    """)
    List<ActivityPulse> findPulses(
        String employeeId,
        LocalDateTime start,
        LocalDateTime end
    );

    ActivityPulse findTopByEmployeeIdOrderByPulseTimeDesc(String employeeId);
}
