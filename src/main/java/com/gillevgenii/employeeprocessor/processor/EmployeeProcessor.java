package com.gillevgenii.employeeprocessor.processor;

import com.gillevgenii.employeeprocessor.filter.EmployeeFilter;
import com.gillevgenii.employeeprocessor.grouping.DepartmentGrouping;
import com.gillevgenii.employeeprocessor.model.Department;
import com.gillevgenii.employeeprocessor.model.Employee;
import com.gillevgenii.employeeprocessor.model.Manager;
import com.gillevgenii.employeeprocessor.parser.ArgumentParser;
import com.gillevgenii.employeeprocessor.parser.DataParser;
import com.gillevgenii.employeeprocessor.service.DataProcessingResult;
import com.gillevgenii.employeeprocessor.service.FileService;
import com.gillevgenii.employeeprocessor.service.SortingService;
import com.gillevgenii.employeeprocessor.sorting.DepartmentSorting;
import com.gillevgenii.employeeprocessor.validator.ArgumentValidator;

import java.util.*;

public class EmployeeProcessor {
    private final String[] args;

    public EmployeeProcessor(String[] args) {
        this.args = args;
    }

    public void run() {
        Map<String, String> arguments = parseArguments();
        List<String> lines = readFile(arguments.get("path"));

        DataProcessingResult dataProcessingResult = parseAndFilterData(lines);
        Map<String, Department> departments = groupDepartments(dataProcessingResult);

        sortDepartmentsAndEmployees(arguments, departments);
        writeResults(arguments, departments, dataProcessingResult.getInvalidData());
    }

    private Map<String, String> parseArguments() {
        Map<String, String> arguments = ArgumentParser.parse(args);
        ArgumentValidator.validate(arguments);
        return arguments;
    }

    private List<String> readFile(String filePath) {
        return new FileService().readFile(filePath);
    }

    private DataProcessingResult parseAndFilterData(List<String> lines) {
        DataParser dataParser = new DataParser();
        Map<Integer, Manager> managers = new HashMap<>();
        Set<Employee> employees = new HashSet<>();
        List<String> invalidData = new ArrayList<>();

        lines.forEach(line -> {
            try {
                Employee employee = dataParser.parseLine(line);
                if (employee instanceof Manager) {
                    managers.put(employee.getId(), (Manager) employee);
                } else {
                    employees.add(employee);
                }
            } catch (IllegalArgumentException e) {
                invalidData.add(line + " | Ошибка: " + e.getMessage());
            }
        });

        return new EmployeeFilter().filterValidEmployees(employees, managers, invalidData);
    }

    private Map<String, Department> groupDepartments(DataProcessingResult dataProcessingResult) {
        return new DepartmentGrouping().groupByDepartments(dataProcessingResult.getManagers(), dataProcessingResult.getValidEmployees());
    }

    private void sortDepartmentsAndEmployees(Map<String, String> arguments, Map<String, Department> departments) {

        // Сначала сортируем департаменты с помощью DepartmentSorting
        departments = DepartmentSorting.sortDepartments(departments);

        // Затем применяем дополнительную сортировку, если она указана
        String sortBy = arguments.getOrDefault("s", "none");
        String order = arguments.getOrDefault("order", "asc");

        if (!"none".equalsIgnoreCase(sortBy)) {
            new SortingService().sortDepartments(departments, sortBy, order);
        }
    }

    private void writeResults(Map<String, String> arguments, Map<String, Department> departments, List<String> invalidData) {
        String output = arguments.getOrDefault("o", "console");
        FileService fileService = new FileService();

        if ("console".equalsIgnoreCase(output)) {
            printResultsToConsole(departments, invalidData);
        } else {
            fileService.outputResults(departments, invalidData, output, arguments.get("path"));
        }
    }

    private void printResultsToConsole(Map<String, Department> departments, List<String> invalidData) {
        departments.values().forEach(this::printDepartment);
        printInvalidData(invalidData);
    }

    private void printDepartment(Department department) {
        System.out.println(department.getName());
        printManager(department);
        printEmployees(department);
        printDepartmentStatistics(department);
    }

    private void printManager(Department department) {
        System.out.print("Manager,%d, %s, %.1f%n".formatted(
                department.getManager().getId(),
                department.getManager().getName(),
                department.getManager().getSalary()
        ));
    }

    private void printEmployees(Department department) {
        department.getEmployees().forEach(employee ->
                System.out.print("Employee,%d, %s, %.1f%n".formatted(
                        employee.getId(),
                        employee.getName(),
                        employee.getSalary()
                ))
        );
    }

    private void printDepartmentStatistics(Department department) {
        System.out.print("%d, %.2f%n".formatted(
                department.getEmployeeCount(),
                department.getAverageSalary()
        ));
    }

    private void printInvalidData(List<String> invalidData) {
        if (!invalidData.isEmpty()) {
            System.out.println("Некорректные данные:");
            invalidData.forEach(System.out::println);
        }
    }
}
