package com.eidiko.security.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eidiko.security.model.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, Integer>{

}
