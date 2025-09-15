package com.example.officeManagementSystem.services;

import com.example.officeManagementSystem.models.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.officeManagementSystem.repositories.EmployeeRepository;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    //create
    public Employee addEmployee(Employee employee)
    {
       return employeeRepository.save(employee);
    }

    //read
    public Employee findEmployee(Long id)
    {
        return employeeRepository.findById(id).orElseThrow(()-> new RuntimeException("employee not found"));
    }
    //update
    public Employee updateEmployee(Employee employee, Long id)
    {
        Employee temp = findEmployee(id);
        temp.setName(employee.getName());
        temp.setEmail(employee.getEmail());
        temp.setSalary(employee.getSalary());
        temp.setJoiningDate(employee.getJoiningDate());
        temp.setDepartment(employee.getDepartment());

        return employeeRepository.save(temp);
    }
    //delete
    public void deleteEmployee(Long id)
    {
        employeeRepository.deleteById(id);
    }
}
