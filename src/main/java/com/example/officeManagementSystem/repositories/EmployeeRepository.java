package com.example.officeManagementSystem.repositories;

import com.example.officeManagementSystem.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {}
