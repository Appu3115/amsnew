package com.example.amsnew.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.example.amsnew.model.Attendance;



@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer>{
     
     List<Attendance> findByAttendanceDate(LocalDate attendanceDate); 

			long countByEmployee_Department_IdAndAttendanceDate(Integer id, LocalDate date);

			long countByEmployee_Department_IdAndAttendanceDateAndLateMinutesGreaterThan(Integer id, LocalDate date,
					long l);


		    Optional<Attendance> findByEmployee_EmployeeIdAndLogoutIsNull(
		            String employeeId
		    );

		    // ✅ Attendance by employee + date
		    Optional<Attendance> findByEmployee_EmployeeIdAndAttendanceDate(
		            String employeeId,
		            LocalDate attendanceDate
		    );

		    // ✅ All attendance of an employee
		    List<Attendance> findByEmployee_EmployeeId(String employeeId);

			List<Attendance> findByEmployee_EmployeeIdOrderByAttendanceDateDesc(String employeeId);

			List<Attendance> findAllByOrderByAttendanceDateDesc();
    	

}
