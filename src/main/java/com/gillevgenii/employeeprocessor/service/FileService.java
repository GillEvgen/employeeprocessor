package com.gillevgenii.employeeprocessor.service;

import com.gillevgenii.employeeprocessor.model.Department;

import java.io.*;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FileService {

    public List<String> readFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return reader.lines().toList();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла: " + e.getMessage());
        }
    }

    public void outputResults(Map<String, Department> departments, List<String> invalidData, String output, String filePath) {
        if ("console".equals(output)) {
            departments.values().forEach(this::printDepartmentToConsole);
            printInvalidDataToConsole(invalidData);
        } else if ("file".equals(output)) {
            writeToFile(departments, invalidData, filePath);
        } else {
            throw new IllegalArgumentException("Некорректный тип вывода: " + output);
        }
    }

    private void writeToFile(Map<String, Department> departments, List<String> invalidData, String filePath) {
        File file = new File(filePath);

        try {
            if (!file.exists() && !file.createNewFile()) {
                throw new IOException("Не удалось создать файл");
            }

            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                departments.values().stream().forEach(department -> printDepartmentToFile(writer, department));
                printInvalidDataToFile(writer, invalidData);
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи в файл: " + e.getMessage());
        }
    }


    private void printDepartmentToConsole(Department department) {
        System.out.println(department.getName());
        System.out.println(department.getManager());
        department.getEmployees().forEach(employee ->
                System.out.printf(Locale.US, "Employee,%d, %s, %.1f%n",
                        employee.getId(), employee.getName(), employee.getSalary()));
        System.out.printf(Locale.US, "%d, %.2f%n", department.getEmployeeCount(), department.getAverageSalary());
    }

    private void printInvalidDataToConsole(List<String> invalidData) {
        if (!invalidData.isEmpty()) {
            System.out.println("Некорректные данные:");
            invalidData.stream().forEach(System.out::println);
        }
    }

    private void printDepartmentToFile(PrintWriter writer, Department department) {
        writer.println(department.getName());
        writer.printf(Locale.US, "Manager,%d, %s, %.1f%n",
                department.getManager().getId(), department.getManager().getName(), department.getManager().getSalary());
        department.getEmployees().forEach(employee ->
                writer.printf(Locale.US, "Employee,%d, %s, %.1f%n",
                        employee.getId(), employee.getName(), employee.getSalary()));
        writer.printf(Locale.US, "%d, %.2f%n", department.getEmployeeCount(), department.getAverageSalary());
    }


    private void printInvalidDataToFile(PrintWriter writer, List<String> invalidData) {
        if (!invalidData.isEmpty()) {
            writer.println("Некорректные данные:");
            invalidData.stream().forEach(writer::println);
        }
    }
}
