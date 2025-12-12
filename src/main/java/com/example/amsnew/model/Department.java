package com.example.amsnew.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="departments")
public class Department {
       @Id
       @GeneratedValue(strategy =  GenerationType.IDENTITY)
       private int id;
       private String dept_name;
       
       
	   public int getId() {
		   return id;
	   }
	   public void setId(int id) {
		   this.id = id;
	   }
	   public String getDept_name() {
		   return dept_name;
	   }
	   public void setDept_name(String dept_name) {
		   this.dept_name = dept_name;
	   }
       
       
       
}
