package com.gillevgenii.employeeprocessor.service;

import com.gillevgenii.employeeprocessor.model.Department;

import java.io.*;
import java.util.List;
import java.util.Map;

public class FileService {

    public static List<String> readFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return reader.lines().toList();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла: " + filePath, e);
        }
    }

    public static void writeResults(Map<String, Department> departments, List<String> invalidData, String output, String filePath) {
        try {
            if ("console".equalsIgnoreCase(output)) {
                printResultsToConsole(departments, invalidData);
            } else if ("file".equalsIgnoreCase(output)) {
                writeToFile(departments, invalidData, filePath);
            } else {
                throw new IllegalArgumentException("Некорректный тип вывода: " + output);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при записи результатов", e);
        }
    }

    private static void writeToFile(Map<String, Department> departments, List<String> invalidData, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            departments.values().forEach(department -> printDepartmentToFile(writer, department));
            printInvalidDataToFile(writer, invalidData);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи в файл: " + filePath, e);
        }
    }

    private static void printResultsToConsole(Map<String, Department> departments, List<String> invalidData) {
        try {
            departments.values().forEach(FileService::printDepartmentToConsole);
            printInvalidDataToConsole(invalidData);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при выводе в консоль", e);
        }
    }

    private static void printDepartmentToConsole(Department department) {
        System.out.println(department.getName());
        System.out.println(department.getManager());
        department.getEmployees().forEach(System.out::println);
    }

    private static void printInvalidDataToConsole(List<String> invalidData) {
        if (!invalidData.isEmpty()) {
            System.out.println("Некорректные данные:");
            invalidData.forEach(System.out::println);
        }
    }

    private static void printDepartmentToFile(PrintWriter writer, Department department) {
        writer.println(department.getName());
        writer.println(department.getManager());
        department.getEmployees().forEach(writer::println);
    }

    private static void printInvalidDataToFile(PrintWriter writer, List<String> invalidData) {
        if (!invalidData.isEmpty()) {
            writer.println("Некорректные данные:");
            invalidData.forEach(writer::println);
        }
    }
}
