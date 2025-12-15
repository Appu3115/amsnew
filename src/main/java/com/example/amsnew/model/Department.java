package com.example.amsnew.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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
       private Integer id;
       
//       @JsonProperty("deptName")
       private String deptName;
       
       
	   public Integer getId() {
		   return id;
	   }
	   public void setId(Integer id) {
		   this.id = id;
	   }
	   public String getDeptName() {
		   return deptName;
	   }
	   public void setDeptName(String deptName) {
		   this.deptName = deptName;
	   }
       
       
       
}
