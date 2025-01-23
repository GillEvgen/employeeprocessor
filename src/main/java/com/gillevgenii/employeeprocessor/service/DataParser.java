package com.gillevgenii.employeeprocessor.service;

import com.gillevgenii.employeeprocessor.model.Employee;
import com.gillevgenii.employeeprocessor.model.Manager;

public class DataParser {

    /**
     * Парсит строку данных в объект Employee или Manager.
     *
     * @param line строка данных
     * @return объект Employee или Manager
     * @throws IllegalArgumentException если данные некорректны
     */
    public Employee parseLine(String line) {
        String[] parts = line.split(",");
        if (parts.length < 5) {
            throw new IllegalArgumentException("Некорректный формат данных: " + line);
        }

        String role = parts[0].trim();
        int id = parseInteger(parts[1].trim(), "ID");
        String name = parts[2].trim();
        double salary = parseDouble(parts[3].trim(), "зарплата");
        String departmentOrManagerId = parts[4].trim();

        if ("Manager".equalsIgnoreCase(role)) {
            return new Manager(role, id, name,salary, departmentOrManagerId);
        } else if ("Employee".equalsIgnoreCase(role)) {
            int managerId = parseInteger(departmentOrManagerId, "ID менеджера");
            return new Employee(role,id, name, salary, managerId);
        } else {
            throw new IllegalArgumentException("Неизвестная роль: " + role);
        }
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
