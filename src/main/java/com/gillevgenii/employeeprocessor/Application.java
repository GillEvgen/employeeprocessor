package com.gillevgenii.employeeprocessor;

import com.gillevgenii.employeeprocessor.service.EmployeeProcessor;

public class Application {
    public static void main(String[] args) {
        try {
            new EmployeeProcessor(args).run();
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка: " + e.getMessage()); // Ошибки пользователя
        } catch (Exception e) {
            System.err.println("Критическая ошибка: " + e.getMessage());
            e.printStackTrace(); // Для отладки
        }
    }
}
