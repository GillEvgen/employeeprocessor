package com.gillevgenii.employeeprocessor.service;

import com.gillevgenii.employeeprocessor.model.Department;
import com.gillevgenii.employeeprocessor.model.Employee;
import com.gillevgenii.employeeprocessor.model.Manager;
import com.gillevgenii.employeeprocessor.utils.ArgumentParser;

import java.io.File;
import java.util.*;

public class EmployeeProcessor {
    private final String[] args;

    public EmployeeProcessor(String[] args) {
        this.args = args;
    }

    public void run() {
        try {
            // Проверка наличия аргументов командной строки
            if (args == null || args.length == 0) {
                throw new IllegalArgumentException("Не переданы аргументы командной строки. Укажите --path=<путь к файлу> и другие параметры.");
            }

            // Вывод переданных аргументов для отладки
            System.out.println("Переданные аргументы: " + Arrays.toString(args));

            // Парсинг аргументов командной строки
            Map<String, String> arguments = ArgumentParser.parse(args);
            System.out.println("Проверяем наличие аргументов: " + arguments);

            // Проверяем наличие обязательных аргументов
            ArgumentParser.validateRequiredArguments(arguments, "path");

            // Проверяем корректность значений для сортировки и порядка
            ArgumentParser.validateArgumentValue(arguments, "sort", "name", "salary");
            ArgumentParser.validateArgumentValue(arguments, "order", "asc", "desc");

            // Читаем путь к входному файлу
            String filePath = arguments.get("path");
            System.out.println("Путь к входному файлу: " + filePath);

            // Проверяем существование входного файла
            File inputFile = new File(filePath);
            if (!inputFile.exists() || !inputFile.isFile()) {
                throw new IllegalArgumentException("Входной файл не найден: " + filePath);
            }

            // Читаем данные из файла
            FileService fileService = new FileService();
            List<String> lines = fileService.readFile(filePath);

            // Проверяем, что файл не пуст
            if (lines == null || lines.isEmpty()) {
                throw new IllegalArgumentException("Входной файл пуст или отсутствует.");
            }

            // Парсинг данных
            DataParser dataParser = new DataParser();
            Map<Integer, Manager> managers = new HashMap<>();
            Set<Employee> employees = new HashSet<>();
            List<String> invalidData = new ArrayList<>();

            for (String line : lines) {
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
            }

            // Проверяем, что найдены менеджеры
            if (managers.isEmpty()) {
                throw new IllegalArgumentException("Не найдено ни одного менеджера в данных.");
            }

            // Фильтруем сотрудников без менеджеров
            Set<Employee> validEmployees = dataParser.filterValidEmployees(employees, managers);

            // Группируем сотрудников по департаментам
            Map<String, Department> departments = dataParser.groupByDepartments(managers, validEmployees);

            // Сортировка данных
            String sortBy = arguments.getOrDefault("sort", "none");
            String order = arguments.getOrDefault("order", "asc");
            if (!"none".equalsIgnoreCase(sortBy)) {
                SortService sortService = new SortService();
                sortService.sortDepartments(departments, sortBy, order);
            }

            // Вывод результатов
            String output = arguments.getOrDefault("output", "console");
            fileService.outputResults(departments, invalidData, output, arguments.get("path"));

        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла ошибка: " + e.getMessage());
            e.printStackTrace(); // Для отладки
        }
    }
}

