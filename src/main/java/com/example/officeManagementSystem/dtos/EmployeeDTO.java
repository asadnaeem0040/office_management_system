package com.example.officeManagementSystem.dtos;


import com.example.officeManagementSystem.models.Department;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDTO {

    private String name;
    private String email;
    private double salary;
    private Date joiningDate;
    private Long departmentId;

}
