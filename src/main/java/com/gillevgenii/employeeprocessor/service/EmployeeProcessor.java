package com.gillevgenii.employeeprocessor.service;

import com.gillevgenii.employeeprocessor.model.Department;
import com.gillevgenii.employeeprocessor.model.Employee;
import com.gillevgenii.employeeprocessor.model.Manager;
import com.gillevgenii.employeeprocessor.utils.ArgumentParser;

import java.util.*;

public class EmployeeProcessor {
    private final String[] args;

    public EmployeeProcessor(String[] args) {
        this.args = args;
    }

    public void run() {
        try {
            // Парсинг аргументов
            Map<String, String> arguments = ArgumentParser.parse(args);

            // Чтение данных из файла
            FileService fileService = new FileService();
            List<String> lines = fileService.readFile(arguments.get("path"));

            // Парсинг данных
            DataParser dataParser = new DataParser();
            Map<Integer, Manager> managers = new HashMap<>();
            Set<Employee> employees = new HashSet<>();
            List<String> invalidData = new ArrayList<>();

            lines.stream().forEach(line -> {
                try {
                    Employee employee = dataParser.parseLine(line);
                    if (employee instanceof Manager) {
                        managers.put(employee.getId(), (Manager) employee);
                    } else {
                        employees.add(employee);
                    }
                } catch (IllegalArgumentException e) {
                    invalidData.add(line);
                }
            });

            // Фильтрация данных
            DataFilter dataFilter = new DataFilter();
            Set<Employee> validEmployees = dataFilter.filterValidEmployees(employees, managers);

            // Группировка по департаментам
            Map<String, Department> departments = dataFilter.groupByDepartments(managers, validEmployees);

            // Вывод результатов
            fileService.outputResults(departments, invalidData, arguments.getOrDefault("output", "console"), arguments.get("path"));

        } catch (Exception e) {
            System.err.println("Произошла ошибка: " + e.getMessage());
        }
    }
}

