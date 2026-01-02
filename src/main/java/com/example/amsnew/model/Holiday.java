package com.example.amsnew.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "holidays")
public class Holiday {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;   // New Year, Pongal

    @Column(nullable = false)
    private LocalDate date; // 2026-01-01

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HolidayType type; // NATIONAL / FESTIVE / RESTRICTED

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public HolidayType getType() {
		return type;
	}

	public void setType(HolidayType type) {
		this.type = type;
	}
    
    
    
}
