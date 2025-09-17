package com.example.officeManagementSystem.services;

import com.example.officeManagementSystem.dtos.AdjustmentSalaryDTO;
import com.example.officeManagementSystem.dtos.EmployeeDTO;
import com.example.officeManagementSystem.exceptions.EmailException;
import com.example.officeManagementSystem.exceptions.PerformanceScoreException;
import com.example.officeManagementSystem.exceptions.SalaryException;
import com.example.officeManagementSystem.models.AdjustmentSalary;
import com.example.officeManagementSystem.models.Department;
import com.example.officeManagementSystem.models.Employee;
import com.example.officeManagementSystem.repositories.AdjustmentSalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.officeManagementSystem.repositories.EmployeeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class EmployeeService {
    @Autowired
    private AdjustmentSalaryRepository adjustmentSalaryRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentService departmentService;

    @Autowired @Lazy
    private EmployeeService employeeService;

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);


    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);


    public Boolean isEmailValid(String email) {

        log.info("Verifying if email is valid {}",email);

        if (email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();


    }

    //create
    public ResponseEntity<?> addEmployee(EmployeeDTO employee)
    {

        if(!isEmailValid(employee.getEmail()))
        {
            log.info("Invalid email {}",employee.getEmail());
            throw new EmailException("Invalid email");
        }
        log.info("Adding employee {}",employee);

        Department department = departmentService.findDepartment(employee.getDepartmentId());

        if(department == null) {
            log.info("Department {} not found",employee.getDepartmentId());
            return ResponseEntity
                    .badRequest()
                    .body("Department not found with the given ID");
        }

        Employee newEmployee = Employee.builder()
                .name(employee.getName())
                .email(employee.getEmail())
                .salary(employee.getSalary()).
                joiningDate(employee.getJoiningDate())
                .department(department)
                .build();


        Employee saved = employeeRepository.save(newEmployee);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);

    }

    //read
    public Employee findEmployee(Long id)
    {
        return employeeRepository.findById(id).orElse(null);
    }
    //update
    public Employee updateEmployee(EmployeeDTO employee, Long id)
    {
        if (employee.getSalary()<0) {
            throw new SalaryException("Salary < 0");
        }
        Employee temp = findEmployee(id);
        temp.setName(employee.getName());
        temp.setEmail(employee.getEmail());
        temp.setSalary(employee.getSalary());
        temp.setJoiningDate(employee.getJoiningDate());
        temp.setDepartment(departmentService.findDepartment(employee.getDepartmentId()));

        return employeeRepository.save(temp);
    }
    //delete
    public void deleteEmployee(Long id)
    {
        employeeRepository.deleteById(id);
    }

    public void validateAndAdjustDepartmentSalary(AdjustmentSalaryDTO temp) {
        AdjustmentSalary request = new AdjustmentSalary().builder()
                .department(departmentService.findDepartment(temp.getDepartmentId()))
                .performanceScore(temp.getPerformanceScore()).build();
        try {
            log.info("Validating and adjusting salaries for department {}", request);

            if(request.getDepartment() == null || request.getDepartment().getId() == null) {
                return;
            }
            employeeService.adjustSalary(request);
        } catch (Exception exception) {
            log.error("Error Occurred while adjusting salaries ", exception);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void adjustSalary(AdjustmentSalary request) {

        LocalDateTime now = LocalDateTime.now();
        AdjustmentSalary record = adjustmentSalaryRepository.findByDepartmentId(request.getDepartment().getId());

        if (record != null && record.getLastAdjustedAt() != null) {
            long diffMinutes = java.time.Duration.between(record.getLastAdjustedAt(), now).toMinutes();
            if (diffMinutes < 30) {
                log.warn("Salary adjustment skipped for department {} (within 30 minutes)",
                        record.getDepartment().getName());
                return;
            }
        }


        if (request.getPerformanceScore() < 0 || request.getPerformanceScore() > 100) {
            throw new PerformanceScoreException("Performance score is out of range");
        }


        List<Employee> employees = employeeRepository.findAllByDepartment_Id(request.getDepartment().getId());
        for (Employee employee : employees) {

            double newSalary = employee.getSalary();
            if (request.getPerformanceScore() >= 90)
            {
                newSalary = newSalary+(newSalary*0.15);
            }
            else if (request.getPerformanceScore() >= 70)
            {
                newSalary = newSalary+(newSalary*0.1);
            }
            else log.warn("No Change");

            if (employee.getJoiningDate() != null) {
                LocalDate joinDate = employee.getJoiningDate().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate();
                int years = Period.between(joinDate, LocalDate.now()).getYears();
                if (years > 5)
                {
                    newSalary = newSalary+(newSalary*0.05);
                }
            } else {
                log.warn("Employee {} has no joining date, skipping tenure bonus", employee.getName());
            }

            if (newSalary > 200000)
                newSalary = 200000;

            employee.setSalary(newSalary);
        }

        employeeRepository.saveAll(employees);

        if (record == null) {
            record = new AdjustmentSalary();
            Department department = departmentService.findDepartment(request.getDepartment().getId());

            if(department == null) {
                throw new RuntimeException("Department not found with the given ID");
            }

            record.setDepartment(department);
        }
        record.setLastAdjustedAt(now);
        record.setPerformanceScore(request.getPerformanceScore());
        adjustmentSalaryRepository.save(record);
    }


}
