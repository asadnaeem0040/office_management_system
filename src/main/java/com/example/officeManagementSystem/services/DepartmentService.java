package com.example.officeManagementSystem.services;

import com.example.officeManagementSystem.dtos.DepartmentDTO;
import com.example.officeManagementSystem.models.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.officeManagementSystem.repositories.DepartmentRepository;


@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    //create
    public Department addDepartment(DepartmentDTO department)
    {
        Department tempDepartment =  new Department().builder().name(department.getName()).code(department.getCode()).build();
        return departmentRepository.save(tempDepartment);
    }
    //read
    public Department findDepartment(Long id)
    {
        return departmentRepository.findById(id).orElse(null);
    }
    //update
    public Department updateDepartment(Long id, DepartmentDTO department)
    {
        Department temp = findDepartment(id);
        temp.setName(department.getName());
        temp.setCode(department.getCode());
        return  departmentRepository.save(temp);
    }
    //delete
    public void deleteDeparment(Long id)
    {
        departmentRepository.deleteById(id);
    }
}
