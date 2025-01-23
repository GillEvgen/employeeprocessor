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

    // Подсчёт количества сотрудников
    public int getEmployeeCount() {
        return employees.size();
    }

    // Подсчёт средней зарплаты
    public double getAverageSalary() {
        if (employees.isEmpty()) return 0.0;

        double totalSalary = employees.stream()
                .mapToDouble(Employee::getSalary)
                .sum();
        return Math.ceil((totalSalary / employees.size()) * 100) / 100.0; // Округляем до 2 знаков вверх
    }
}

