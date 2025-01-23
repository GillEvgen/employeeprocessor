package com.gillevgenii.employeeprocessor.service;

import com.gillevgenii.employeeprocessor.model.Department;
import com.gillevgenii.employeeprocessor.model.Employee;
import com.gillevgenii.employeeprocessor.model.Manager;

import java.io.*;
import java.util.List;
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
        department.getEmployees().stream().forEach(System.out::println);
        System.out.printf("amount,salary %d,%.2f%n", department.getEmployeeCount(), department.getAverageSalary());
    }

    private void printInvalidDataToConsole(List<String> invalidData) {
        if (!invalidData.isEmpty()) {
            System.out.println("Некорректные данные:");
            invalidData.stream().forEach(System.out::println);
        }
    }

    private void printDepartmentToFile(PrintWriter writer, Department department) {
        writer.println(department.getName());
        writer.println(department.getManager());
        department.getEmployees().stream().forEach(writer::println);
        writer.printf("amount,salary %d,%.2f%n", department.getEmployeeCount(), department.getAverageSalary());
    }

    private void printInvalidDataToFile(PrintWriter writer, List<String> invalidData) {
        if (!invalidData.isEmpty()) {
            writer.println("Некорректные данные:");
            invalidData.stream().forEach(writer::println);
        }
    }
}
