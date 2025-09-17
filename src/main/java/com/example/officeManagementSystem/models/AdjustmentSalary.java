package com.example.officeManagementSystem.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;


@Entity

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AdjustmentSalary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Department cannot be null")
    @JoinColumn(nullable = false, unique = true)
    @OneToOne(cascade = CascadeType.ALL)
    private Department department;

    @Min(value = 0, message = "Performance score must greater than 0")
    @Max(value=100,message = "PerPerformance score must not exceed 100")
    private int performanceScore;


    private LocalDateTime lastAdjustedAt;

}
