package com.example.officeManagementSystem.repositories;

import com.example.officeManagementSystem.models.AdjustmentSalary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdjustmentSalaryRepository extends JpaRepository<AdjustmentSalary, Long> {
    AdjustmentSalary findByDepartmentId(Long departmentId);
}