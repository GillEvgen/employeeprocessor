package com.gillevgenii.employeeprocessor.parser;

import com.gillevgenii.employeeprocessor.model.Employee;
import com.gillevgenii.employeeprocessor.model.Manager;
import com.gillevgenii.employeeprocessor.utils.ParsingUtils;
import com.gillevgenii.employeeprocessor.utils.ValidationUtils;

public class DataParser {

    private static final int REQUIRED_FIELDS_COUNT = 5;
    private static final String ROLE_MANAGER = "Manager";
    private static final String ROLE_EMPLOYEE = "Employee";

    public Employee parseLine(String line) {
        String[] parts = line.split(",", REQUIRED_FIELDS_COUNT);
        if (parts.length < REQUIRED_FIELDS_COUNT) {
            throw new IllegalArgumentException("Некорректный формат строки: " + line);
        }

        String role = parts[0].trim();
        ValidationUtils.validateNonEmpty(role, "Роль");

        int id = ParsingUtils.parseValidId(parts[1]);
        String name = parts[2].trim();
        ValidationUtils.validateNonEmpty(name, "Имя");
        ValidationUtils.validateNameLength(name);

        double salary = ParsingUtils.parseDouble(parts[3], "Зарплата");
        ValidationUtils.validateSalary(salary);

        String departmentOrManagerId = parts[4].trim();
        ValidationUtils.validateNonEmpty(departmentOrManagerId, "Отдел/ID Менеджера");

        return createEmployee(role, id, name, salary, departmentOrManagerId);
    }

    private Employee createEmployee(String role, int id, String name, double salary, String departmentOrManagerId) {
        if (ROLE_MANAGER.equalsIgnoreCase(role)) {
            ValidationUtils.validateDepartmentName(departmentOrManagerId);
            return new Manager(role, id, name, salary, departmentOrManagerId);
        }
        int managerId = ParsingUtils.parseValidId(departmentOrManagerId);
        ValidationUtils.validateId(managerId);
        return new Employee(role, id, name, salary, managerId);
    }
}
