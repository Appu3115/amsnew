package com.example.amsnew.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DepartmentRequest {
	
	   @JsonProperty("deptName")
       private String deptName;

	   public String getDeptName() {
		   return deptName;
	   }

	   public void setDeptName(String deptName) {
		   this.deptName = deptName;
	   }

	   public DepartmentRequest() {
		super();
	   }
       
       
}
