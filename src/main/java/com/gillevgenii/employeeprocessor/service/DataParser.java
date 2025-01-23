package com.gillevgenii.employeeprocessor.service;

import com.gillevgenii.employeeprocessor.model.Employee;
import com.gillevgenii.employeeprocessor.model.Manager;

public class DataParser {

    // Константы
    private static final String ERROR_INVALID_FORMAT = "Некорректный формат данных: ";
    private static final String ERROR_UNKNOWN_ROLE = "Неизвестная роль: ";

    public Employee parseLine(String line) {
        String[] parts = splitLine(line);

        // Извлечение значений
        String role = extractRole(parts);
        int id = extractId(parts);
        String name = extractName(parts);
        double salary = extractSalary(parts);
        String departmentOrManagerId = extractDepartmentOrManagerId(parts);

        // Обработка роли
        if ("Manager".equalsIgnoreCase(role)) {
            return new Manager(role, id, name, salary, departmentOrManagerId);
        } else if ("Employee".equalsIgnoreCase(role)) {
            int managerId = parseInteger(departmentOrManagerId, "ID менеджера");
            return new Employee(role, id, name, salary, managerId);
        } else {
            throw new IllegalArgumentException(ERROR_UNKNOWN_ROLE + role);
        }
    }

    // Вспомогательные методы

    private String[] splitLine(String line) {
        String[] parts = line.split(",");
        if (parts.length < 5) {
            throw new IllegalArgumentException(ERROR_INVALID_FORMAT + line);
        }
        return parts;
    }

    private String extractRole(String[] parts) {
        return parts[0].trim();
    }

    private int extractId(String[] parts) {
        return parseInteger(parts[1].trim(), "ID");
    }

    private String extractName(String[] parts) {
        return parts[2].trim();
    }

    private double extractSalary(String[] parts) {
        return parseDouble(parts[3].trim(), "зарплата");
    }

    private String extractDepartmentOrManagerId(String[] parts) {
        return parts[4].trim();
    }

    private int parseInteger(String value, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректное значение для " + fieldName + ": " + value);
        }
    }

    private double parseDouble(String value, String fieldName) {
        try {
            double result = Double.parseDouble(value);
            if (result < 0) {
                throw new IllegalArgumentException(fieldName + " не может быть отрицательной: " + value);
            }
            return result;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректное значение для " + fieldName + ": " + value);
        }
    }
}
