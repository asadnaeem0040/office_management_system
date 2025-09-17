package com.example.officeManagementSystem.dtos;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdjustmentSalaryDTO {
    private Long departmentId;
    private int performanceScore;
}
