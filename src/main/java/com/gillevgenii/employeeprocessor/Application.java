package com.gillevgenii.employeeprocessor;

import com.gillevgenii.employeeprocessor.processor.EmployeeProcessor;

public class Application {
    public static void main(String[] args) {
        try {
            new EmployeeProcessor(args).run();
        } catch (IllegalArgumentException e) {
            System.err.println("[Ошибка пользователя] " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("[Ошибка выполнения] " + e.getMessage());
            e.printStackTrace(); // Логирование ошибки
        } finally {
            System.out.println("Программа завершила выполнение.");
        }
    }
}
