package com.gillevgenii.employeeprocessor.service;

import com.gillevgenii.employeeprocessor.model.Employee;
import com.gillevgenii.employeeprocessor.model.Manager;
import com.gillevgenii.employeeprocessor.utils.ValidationUtils;

import java.util.Arrays;

public class DataParser {

    private static final int REQUIRED_FIELDS_COUNT = 5;
    private static final String ROLE_MANAGER = "Manager";
    private static final String ROLE_EMPLOYEE = "Employee";

    public Employee parseLine(String line) {
        String[] parts = splitAndValidateLine(line);

        String role = extractAndValidateRole(parts[0]);
        int id = extractAndValidateId(parts[1]);
        String name = extractAndValidateName(parts[2]);
        double salary = extractAndValidateSalary(parts[3]);
        String departmentOrManagerId = parts[4].trim();

        return createEmployee(role, id, name, salary, departmentOrManagerId);
    }

    private String[] splitAndValidateLine(String line) {
        String[] parts = line.split(",", REQUIRED_FIELDS_COUNT);
        if (parts.length < REQUIRED_FIELDS_COUNT) {
            throw new IllegalArgumentException(
                    String.format("Invalid format: expected %d fields, got %d. Line: %s",
                            REQUIRED_FIELDS_COUNT, parts.length, line)
            );
        }
        return parts;
    }

    private String extractAndValidateRole(String rawRole) {
        String role = rawRole.trim();
        ValidationUtils.validateNonEmpty(role, "Role");

        if (!role.equalsIgnoreCase(ROLE_MANAGER) && !role.equalsIgnoreCase(ROLE_EMPLOYEE)) {
            throw new IllegalArgumentException(
                    String.format("Unknown role: %s. Valid roles: %s",
                            role, Arrays.asList(ROLE_MANAGER, ROLE_EMPLOYEE))
            );
        }
        return role;
    }

    private int extractAndValidateId(String rawId) {
        int id = ValidationUtils.parseValidId(rawId);
        ValidationUtils.validateId(id);
        return id;
    }

    private String extractAndValidateName(String rawName) {
        String name = rawName.trim();
        ValidationUtils.validateNonEmpty(name, "Name");
        ValidationUtils.validateNameLength(name);
        return name;
    }

    private double extractAndValidateSalary(String rawSalary) {
        double salary = ValidationUtils.parseDouble(rawSalary, "Salary");
        ValidationUtils.validateSalary(salary);
        return salary;
    }

    private Employee createEmployee(String role, int id, String name,
                                    double salary, String departmentOrManagerId) {
        if (role.equalsIgnoreCase(ROLE_MANAGER)) {
            return createManager(role, id, name, salary, departmentOrManagerId);
        }
        return createRegularEmployee(role, id, name, salary, departmentOrManagerId);
    }

    private Manager createManager(String role, int id, String name,
                                  double salary, String department) {
        String validatedDepartment = department.trim();
        ValidationUtils.validateNonEmpty(validatedDepartment, "Department");
        ValidationUtils.validateDepartmentName(validatedDepartment);

        return new Manager(role, id, name, salary, validatedDepartment);
    }

    private Employee createRegularEmployee(String role, int id, String name,
                                           double salary, String rawManagerId) {
        int managerId = ValidationUtils.parseValidId(rawManagerId);
        ValidationUtils.validateId(managerId);

        return new Employee(role, id, name, salary, managerId);
    }
}