package com.gillevgenii.employeeprocessor;

import com.gillevgenii.employeeprocessor.service.EmployeeProcessor;

public class Application {
    public static void main(String[] args) {
        try {
            // Точка входа в приложение
            EmployeeProcessor processor = new EmployeeProcessor(args);
            processor.run();
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}
