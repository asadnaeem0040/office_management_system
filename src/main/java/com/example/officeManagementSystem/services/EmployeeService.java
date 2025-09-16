package com.example.officeManagementSystem.services;

import com.example.officeManagementSystem.dtos.EmployeeDTO;
import com.example.officeManagementSystem.exceptions.EmailException;
import com.example.officeManagementSystem.exceptions.PerformanceScoreException;
import com.example.officeManagementSystem.exceptions.SalaryException;
import com.example.officeManagementSystem.models.AdjustmentSalary;
import com.example.officeManagementSystem.models.Department;
import com.example.officeManagementSystem.models.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.officeManagementSystem.repositories.EmployeeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentService departmentService;

    private Map<Long, Long> lastAdjustmentMap = new HashMap<>();

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

    public void adjustSalary(AdjustmentSalary adjustment)
    {
        if (adjustment.performanceScore < 0 || adjustment.performanceScore>100) {
            throw new PerformanceScoreException("Performance score is out of range");
        }
        List<Employee> allEmployees = employeeRepository.findAll();

        for(int i=0;i<allEmployees.size();i++)
        {
            /*Long deptId = adjustment.departmentId;
            long now = System.currentTimeMillis();


            if(lastAdjustmentMap.containsKey(deptId)) {
                long lastTime = lastAdjustmentMap.get(deptId);
                long diffMinutes = (now - lastTime) / (1000 * 60);

                if(diffMinutes < 30) {
                    log.warn("Salary adjustment for department {} skipped (within 30 min idempotency window)", deptId);
                    return; // skip execution
                }
            }


            lastAdjustmentMap.put(deptId, now);*/

            if(allEmployees.get(i).getDepartment().getId().equals(adjustment.departmentId))
            {
                Employee employee = allEmployees.get(i);

                if(adjustment.performanceScore >= 90){
                    employee.setSalary(employee.getSalary()+(employee.getSalary()*0.15));
                }
                else if(adjustment.performanceScore >= 70) {
                    employee.setSalary(employee.getSalary()+(employee.getSalary()*0.1));
                }
                else {
                    log.warn("low performance score");
                }


                Calendar joinCal = Calendar.getInstance();
                joinCal.setTime(employee.getJoiningDate());

                Calendar nowCal = Calendar.getInstance();
                int years = nowCal.get(Calendar.YEAR) - joinCal.get(Calendar.YEAR);

                if(years > 5){
                    employee.setSalary(employee.getSalary()+(employee.getSalary()*0.05));
                }
                if(employee.getSalary() > 200000){
                    employee.setSalary(200000);
                }

                employeeRepository.save(employee);
            }
        }
    }

}
