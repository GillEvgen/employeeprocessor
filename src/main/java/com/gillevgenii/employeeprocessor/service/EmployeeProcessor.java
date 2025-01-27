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
                Map<String, String> arguments = parseAndValidateArguments();
                List<String> lines = readFile(arguments);

                DataProcessingResult dataProcessingResult = parseAndFilterData(lines);
                Map<String, Department> departments = groupDepartments(dataProcessingResult);

                sortDepartmentsAndEmployees(arguments, departments);
                outputResults(arguments, departments, dataProcessingResult.getInvalidData());

            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Произошла ошибка: " + e.getMessage());
                e.printStackTrace(); // Для отладки
            }
        }

        private Map<String, String> parseAndValidateArguments() {
            Map<String, String> arguments = ArgumentParser.parse(args);
            ArgumentParser.validateRequiredArguments(arguments, "path");
            ArgumentParser.validateArgumentValue(arguments, "sort", "name", "salary");
            ArgumentParser.validateArgumentValue(arguments, "order", "asc", "desc");
            return arguments;
        }

        private List<String> readFile(Map<String, String> arguments) {
            FileService fileService = new FileService();
            return fileService.readFile(arguments.get("path"));
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
                    invalidData.add(line);
                }
            });

            DataFilter dataFilter = new DataFilter();
            Set<Employee> validEmployees = dataFilter.filterValidEmployees(employees, managers);

            return new DataProcessingResult(managers, validEmployees, invalidData);
        }

        private Map<String, Department> groupDepartments(DataProcessingResult dataProcessingResult) {
            DataFilter dataFilter = new DataFilter();
            return dataFilter.groupByDepartments(dataProcessingResult.getManagers(), dataProcessingResult.getValidEmployees());
        }

        private void sortDepartmentsAndEmployees(Map<String, String> arguments, Map<String, Department> departments) {
            String sortBy = arguments.getOrDefault("sort", "none");
            String order = arguments.getOrDefault("order", "asc");

            if (!"none".equalsIgnoreCase(sortBy)) {
                SortService sortService = new SortService();
                sortService.sortDepartments(departments, sortBy, order);
            }
        }

        private void outputResults(Map<String, String> arguments, Map<String, Department> departments, List<String> invalidData) {
            String output = arguments.getOrDefault("output", "console");
            if ("console".equalsIgnoreCase(output)) {
                // Вывод результатов в консоль
                printResultsToConsole(departments, invalidData);
            } else {
                // Вывод результатов в файл
                FileService fileService = new FileService();
                String outputPath = arguments.get("outputPath");
                fileService.outputResults(departments, invalidData, output, outputPath);
            }
        }

        private void printResultsToConsole(Map<String, Department> departments, List<String> invalidData) {
            departments.values().forEach(department -> {
                System.out.println(department.getName());

                // Вывод менеджера
                System.out.printf(Locale.US, "Manager,%d, %s, %.1f%n",
                        department.getManager().getId(),
                        department.getManager().getName(),
                        department.getManager().getSalary());

                // Вывод сотрудников
                department.getEmployees().forEach(employee ->
                        System.out.printf(Locale.US, "Employee,%d, %s, %.1f%n",
                                employee.getId(),
                                employee.getName(),
                                employee.getSalary()));

                // Вывод статистики департамента
                System.out.printf(Locale.US, "%d, %.2f%n",
                        department.getEmployeeCount(),
                        department.getAverageSalary());
            });

            // Вывод некорректных данных
            if (!invalidData.isEmpty()) {
                System.out.println("Некорректные данные:");
                invalidData.forEach(System.out::println);
            }
        }
    }
