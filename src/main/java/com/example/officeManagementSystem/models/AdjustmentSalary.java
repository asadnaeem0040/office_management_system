package com.example.officeManagementSystem.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class AdjustmentSalary {
    public long departmentId;

    @Min(value = 0)
    @Max(value = 100)
    public int performanceScore;
}
