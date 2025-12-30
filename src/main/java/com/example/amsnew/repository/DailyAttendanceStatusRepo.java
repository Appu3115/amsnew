package com.example.amsnew.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.amsnew.model.DailyAttendanceStatus;
import com.example.amsnew.model.DailyStatus;
import com.example.amsnew.model.Employees;

public interface DailyAttendanceStatusRepo extends JpaRepository<DailyAttendanceStatus, Long> {

       Optional<DailyAttendanceStatus> findByEmployeeAndAttendanceDate(Employees emp, LocalDate date);

       long countByAttendanceDateAndStatus(LocalDate date, DailyStatus status);
       
       long countByEmployee_EmployeeIdAndAttendanceDateBetweenAndStatus(
    	        String employeeId,
    	        LocalDate start,
    	        LocalDate end,
    	        DailyStatus status);

    	long countByEmployee_EmployeeIdAndAttendanceDateBetween(
    	        String employeeId,
    	        LocalDate start,
    	        LocalDate end);

    	 List<DailyAttendanceStatus> findByAttendanceDate(LocalDate date);

    	    List<DailyAttendanceStatus> findByAttendanceDateBetween(
    	            LocalDate start,
    	            LocalDate end
    	    );

}

