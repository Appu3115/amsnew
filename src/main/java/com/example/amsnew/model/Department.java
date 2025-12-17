package com.example.amsnew.model;

import jakarta.persistence.Column;
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
       
       @Column(nullable=false)
       private String deptName;
       
       @Column(unique=true,nullable=false)
       private String departmentCode;
       
       private boolean active=true;
       
       
	   public String getDepartmentCode() {
		return departmentCode;
	}
	   public void setDepartmentCode(String departmentCode) {
		   this.departmentCode = departmentCode;
	   }
	   public boolean isActive() {
		   return active;
	   }
	   public void setActive(boolean active) {
		   this.active = active;
	   }
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
