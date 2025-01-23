package com.gillevgenii.employeeprocessor.model;

import java.util.HashSet;
import java.util.Set;

public class Department {
    private final String name;
    private final Manager manager;
    private final Set<Employee> employees;

    public Department(String name, Manager manager) {
        this.name = name;
        this.manager = manager;
        this.employees = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public Manager getManager() {
        return manager;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public double calculateAverageSalary() {
        return employees.stream()
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0);
    }

    public int getEmployeeCount() {
        return employees.size();
    }
}

