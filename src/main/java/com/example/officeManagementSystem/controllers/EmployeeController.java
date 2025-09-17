package com.example.officeManagementSystem.controllers;

import com.example.officeManagementSystem.dtos.AdjustmentSalaryDTO;
import com.example.officeManagementSystem.dtos.EmployeeDTO;
import com.example.officeManagementSystem.models.AdjustmentSalary;
import com.example.officeManagementSystem.models.Employee;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.officeManagementSystem.services.EmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;


    //create
    @PostMapping
    public ResponseEntity<?> addEmployee(@RequestBody @Valid EmployeeDTO employee)
    {
        return employeeService.addEmployee(employee);
    }
    //read
    @GetMapping("/{id}")
    public Employee findEmployee(@PathVariable Long id)
    {
        return employeeService.findEmployee(id);
    }


    //update
    @PutMapping("/{id}")
    public Employee updateEmployee(@RequestBody @Valid EmployeeDTO employee, @PathVariable Long id)
    {
        return employeeService.updateEmployee(employee, id);
    }

    //delete
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id)
    {
        employeeService.deleteEmployee(id);
    }

    @PutMapping("/adjust-salary")
    public void  adjustSalary(@RequestBody @Valid AdjustmentSalaryDTO adjustment)
    {
        employeeService.validateAndAdjustDepartmentSalary(adjustment);
    }
}
