package com.example.officeManagementSystem.repositories;

import com.example.officeManagementSystem.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {


    List<Employee> findAllByDepartment_Id(Long id);
}
