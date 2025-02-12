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
        try {
            Map<String, String> arguments = parseArguments();
            List<String> lines = readFile(arguments.get("path"));

            DataProcessingResult dataProcessingResult = parseAndFilterData(lines);
            Map<String, Department> departments = groupDepartments(dataProcessingResult);

            sortDepartmentsAndEmployees(arguments, departments);
            writeResults(arguments, departments, dataProcessingResult.getInvalidData());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка в процессе обработки сотрудников: " + e.getMessage(), e);
        }
    }

    private Map<String, String> parseArguments() {
        try {
            Map<String, String> arguments = ArgumentParser.parse(args);
            ArgumentValidator.validate(arguments);
            return arguments;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при разборе аргументов командной строки: " + e.getMessage(), e);
        }
    }

    private List<String> readFile(String filePath) {
        try {
            return FileService.readFile(filePath);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при чтении файла: " + filePath, e);
        }
    }

    private DataProcessingResult parseAndFilterData(List<String> lines) {
        try {
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

            return EmployeeFilter.filterValidEmployees(employees, managers, invalidData);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обработке данных сотрудников", e);
        }
    }

    private Map<String, Department> groupDepartments(DataProcessingResult dataProcessingResult) {
        try {
            return DepartmentGrouping.groupByDepartments(dataProcessingResult.getManagers(), dataProcessingResult.getValidEmployees());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при группировке департаментов", e);
        }
    }

    private void sortDepartmentsAndEmployees(Map<String, String> arguments, Map<String, Department> departments) {
        try {
            departments = DepartmentSorting.sortDepartments(departments);
            String sortBy = arguments.getOrDefault("s", "none");
            String order = arguments.getOrDefault("order", "asc");

            if (!"none".equalsIgnoreCase(sortBy)) {
                SortingService.sortDepartments(departments, sortBy, order);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при сортировке департаментов и сотрудников", e);
        }
    }

    private void writeResults(Map<String, String> arguments, Map<String, Department> departments, List<String> invalidData) {
        try {
            String output = arguments.getOrDefault("o", "console");

            if ("console".equalsIgnoreCase(output)) {
                printResultsToConsole(departments, invalidData);
            } else {
                FileService.writeResults(departments, invalidData, output, arguments.get("path"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при записи результатов", e);
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
        System.out.printf("Manager,%d, %s, %.1f%n",
                department.getManager().getId(),
                department.getManager().getName(),
                department.getManager().getSalary()
        );
    }

    private void printEmployees(Department department) {
        department.getEmployees().forEach(employee ->
                System.out.printf("Employee,%d, %s, %.1f%n",
                        employee.getId(),
                        employee.getName(),
                        employee.getSalary()
                )
        );
    }

    private void printDepartmentStatistics(Department department) {
        System.out.printf("%d, %.2f%n",
                department.getEmployeeCount(),
                department.getAverageSalary()
        );
    }

    private void printInvalidData(List<String> invalidData) {
        if (!invalidData.isEmpty()) {
            System.out.println("Некорректные данные:");
            invalidData.forEach(System.out::println);
        }
    }
}
