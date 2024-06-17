package com.example.demo.controller;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Employee;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	//@CircuitBreaker(name = "employeeCircuitBreaker", fallbackMethod = "getEmployeeFallback")
	//@Retry(name = "employeeRetry", fallbackMethod = "getEmployeeFallback")
	// Bulkhead.Type.SEMAPHORE 表示使用信號量來控制並發數量
	//@Bulkhead(name = "employeeBulkhead", type = Bulkhead.Type.SEMAPHORE)
	@GetMapping("/semaphone/{empId}")
	public Employee getEmployee(@PathVariable Integer empId) throws InterruptedException {
        
		if (empId == 0 || empId == -1) {
			// 這裡故意拋出異常，模擬異常情況, 這樣 CircuitBreaker 就會觸發
			// 若沒有設定 CircuitBreaker, 則會直接拋出異常, 會給全域異常處理器 GlobalExceptiionHandler 處理
			throw new RuntimeException("Employee not found");
		}
		
		// 模擬業務處理延遲
		// 給 @Bulkhead 測試使用
		// 當 localhost:6060/employee/1 執行並連續多次按下的時候就會印出 "Bulkhead call rejected"
		Thread.sleep(3000);
		
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
	
	// Bulkhead.Type.THREADPOOL 表示使用線程池來控制並發數量
	// 使用Bulkhead.Type.THREADPOOL時, 方法的返回值必須是 CompletableFuture
	@Retry(name = "employeeRetry", fallbackMethod = "getEmployeeFallback2")
	@Bulkhead(name = "employeeBulkhead2", type = Bulkhead.Type.THREADPOOL)
    @GetMapping("/threadpool/{empId}")
    public CompletableFuture<Employee> getEmployee2(@PathVariable Integer empId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (empId == 0 || empId == -1) {
                    // 故意拋出異常，模擬異常情況
                    throw new RuntimeException("Employee not found");
                }
                
                // 模擬業務處理延遲
                Thread.sleep(3000);
                
                Employee emp = new Employee();
                emp.setEmpId(empId);
                emp.setEmpName("John");
                emp.setDesignation("Manager");
                emp.setSalary(80000.0);
                return emp;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
	
    // 這是一個回退方法(Fallback)，當 getEmployee 方法發生異常時，將調用此方法
    public CompletableFuture<Employee> getEmployeeFallback2(Integer empId, Throwable t) {
        return CompletableFuture.supplyAsync(() -> {
            Employee emp = new Employee();
            emp.setEmpId(empId);
            emp.setEmpName("Fallback Employee");
            emp.setDesignation("Fallback Designation");
            emp.setSalary(0.0);
            return emp;
        });
    }
    
	
}
