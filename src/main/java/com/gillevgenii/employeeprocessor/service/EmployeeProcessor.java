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
            Map<String, String> arguments = parseArguments();

            List<String> lines = readFile(arguments);

            DataParser dataParser = new DataParser();
            Map<Integer, Manager> managers = new HashMap<>();
            Set<Employee> employees = new HashSet<>();
            List<String> invalidData = parseData(lines, dataParser, managers, employees);

            Set<Employee> validEmployees = filterEmployees(employees, managers);

            Map<String, Department> departments = groupDepartments(managers, validEmployees);

            outputResults(arguments, departments, invalidData);

        } catch (Exception e) {
            System.err.println("Произошла ошибка: " + e.getMessage());
        }
    }

    private Map<String, String> parseArguments() {
        return ArgumentParser.parse(args);
    }

    private List<String> readFile(Map<String, String> arguments) {
        FileService fileService = new FileService();
        return fileService.readFile(arguments.get("path"));
    }

    private List<String> parseData(List<String> lines, DataParser dataParser, Map<Integer, Manager> managers, Set<Employee> employees) {
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
        return invalidData;
    }

    private Set<Employee> filterEmployees(Set<Employee> employees, Map<Integer, Manager> managers) {
        DataFilter dataFilter = new DataFilter();
        return dataFilter.filterValidEmployees(employees, managers);
    }

    private Map<String, Department> groupDepartments(Map<Integer, Manager> managers, Set<Employee> employees) {
        DataFilter dataFilter = new DataFilter();
        return dataFilter.groupByDepartments(managers, employees);
    }

    private void outputResults(Map<String, String> arguments, Map<String, Department> departments, List<String> invalidData) {
        FileService fileService = new FileService();
        String output = arguments.getOrDefault("output", "console");
        fileService.outputResults(departments, invalidData, output, arguments.get("path"));
    }
}

