package com.example.demo.controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Employee;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	@CircuitBreaker(name = "employeeCircuitBreaker", fallbackMethod = "getEmployeeFallback")
	@GetMapping("/{empId}")
	public Employee getEmployee(@PathVariable Integer empId) {
        
		if (empId == 0 || empId == -1) {
			// 這裡故意拋出異常，模擬異常情況, 這樣 CircuitBreaker 就會觸發
			throw new RuntimeException("Employee not found");
		}
		
		Employee emp = new Employee();
		emp.setEmpId(empId);
		emp.setEmpName("John");
		emp.setDesignation("Manager");
		emp.setSalary(80000.0);
		return emp;
	}
	
	// 這是一個回退方法(Fallback)，當 getEmployee 方法發生異常時，將調用此方法
	public Employee getEmployeeFallback(Integer empId, Throwable t) {
		if (empId == -1) {
			// 這裡故意拋出異常，模擬異常情況, GlobalExceptiionHandler 會處理這個異常
			throw new RuntimeException("Fallback: Employee not found");
		}
		Employee emp = new Employee();
		emp.setEmpId(empId);
		emp.setEmpName("Fallback Employee");
		emp.setDesignation("Fallback Designation");
		emp.setSalary(0.0);
		return emp;
	}
}
