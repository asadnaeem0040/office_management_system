package com.example.officeManagementSystem.controllers;

import com.example.officeManagementSystem.models.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.officeManagementSystem.services.DepartmentService;

@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    //create
    @PostMapping
    public Department addDepartment(@RequestBody Department department)
    {
        return departmentService.addDepartment(department);
    }
    //read
    @GetMapping("/{id}")
    public Department findDepartment(@PathVariable Long id)
    {
        return departmentService.findDepartment(id);
    }
    //update
    @PutMapping("/{id}")
    public Department updateDepartment(@RequestBody Department department, @PathVariable Long id)
    {
        return departmentService.updateDepartment(id,department);
    }

    //delete
    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable Long id)
    {
        departmentService.deleteDeparment(id);
    }
}
