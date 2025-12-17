package com.example.amsnew.model;

<<<<<<< HEAD
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
=======
import jakarta.persistence.*;
>>>>>>> 2d749b33a0991e9b0769fc283d6c67f26e0cb9c9

@Entity
@Table(
    name = "departments",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "deptName")
    }
)
public class Department {
<<<<<<< HEAD
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
       
       
       
=======

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String deptName;

    // ---------- Getters & Setters ----------

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
>>>>>>> 2d749b33a0991e9b0769fc283d6c67f26e0cb9c9
}
