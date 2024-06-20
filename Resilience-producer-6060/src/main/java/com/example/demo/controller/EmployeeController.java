package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Employee;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	@GetMapping("/{empId}")
	public Employee getEmployee(@PathVariable Integer empId) {
		
		Employee emp = new Employee();
		emp.setEmpId(empId);
		emp.setEmpName("John");
		emp.setDescription("Manager");
		emp.setSalary(15_0000);
		
		return emp;
		
	}
	
}
