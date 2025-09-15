package com.example.officeManagementSystem.repositories;

import com.example.officeManagementSystem.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department,Long> {
}
