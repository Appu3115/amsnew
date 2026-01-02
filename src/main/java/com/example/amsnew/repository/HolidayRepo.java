package com.example.amsnew.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.amsnew.model.Holiday;

@Repository
public interface HolidayRepo extends JpaRepository<Holiday, Integer> {

    @Query("""
        SELECT COUNT(h)
        FROM Holiday h
        WHERE h.date BETWEEN :start AND :end
          AND h.type IN ('NATIONAL', 'FESTIVE')
    """)
    long countCompanyHolidays(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );
    
    @Query("""
    	    SELECT COUNT(h) > 0
    	    FROM Holiday h
    	    WHERE h.date BETWEEN :start AND :end
    	      AND h.type = 'RESTRICTED'
    	""")
    	boolean hasRestrictedHolidayBetween(
    	        @Param("start") LocalDate start,
    	        @Param("end") LocalDate end
    	);

  

        @Query("""
            SELECT h FROM Holiday h
            WHERE YEAR(h.date) = :year
            ORDER BY h.date
        """)
        List<Holiday> findByYear(@Param("year") int year);

		boolean existsByDate(LocalDate date);
    

}

