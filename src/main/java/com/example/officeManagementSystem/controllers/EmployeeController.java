package com.example.officeManagementSystem.controllers;

import com.example.officeManagementSystem.models.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.officeManagementSystem.services.EmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;


    //create
    @PostMapping
    public Employee addEmployee(@RequestBody Employee employee)
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
    public Employee updateEmployee(@RequestBody Employee employee, @PathVariable Long id)
    {
        return employeeService.updateEmployee(employee, id);
    }

    //delete
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id)
    {
        employeeService.deleteEmployee(id);
    }
}
