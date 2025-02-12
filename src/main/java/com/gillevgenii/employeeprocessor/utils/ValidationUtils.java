package com.gillevgenii.employeeprocessor.utils;

import java.util.Arrays;

public class ValidationUtils {

    private static final int MAX_NAME_LENGTH = 100;
    private static final double MIN_SALARY = 0.0;
    private static final String[] ALLOWED_DEPARTMENTS = {"HR", "Sales", "IT"};

    public static void validateSalary(double salary) {
        if (salary < MIN_SALARY) {
            throw new IllegalArgumentException(
                    String.format("Некорректное значение зарплаты: %.2f. Допустимые значения от %.2f и выше", salary, MIN_SALARY)
            );
        }
    }

    public static void validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    String.format("Некорректный ID: %d. Должен быть положительным.".formatted(id))
            );
        }
    }

    public static void validateNonEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("%s не может быть пустым.", fieldName)
            );
        }
    }

    public static void validateNameLength(String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("Длина имени превышает максимальное значение (%d): %s",
                            MAX_NAME_LENGTH, name)
            );
        }
    }

    public static void validateDepartmentName(String department) {
        for (String allowed : ALLOWED_DEPARTMENTS) {
            if (allowed.equalsIgnoreCase(department)) return;
        }
        throw new IllegalArgumentException(
                String.format("Некорректный отдел: %s. Допустимые значения: %s",
                        department, Arrays.toString(ALLOWED_DEPARTMENTS))
        );
    }
}