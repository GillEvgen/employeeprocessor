package com.gillevgenii.employeeprocessor.utils;

import java.util.Arrays;

public class ValidationUtils {

    private static final int MAX_NAME_LENGTH = 100;
    private static final double MIN_SALARY = 0.0;
    private static final String[] ALLOWED_DEPARTMENTS = {"HR", "Sales", "IT"};

    public static void validateSalary(double salary) {
        if (salary < MIN_SALARY) {
            throw new IllegalArgumentException(
                    String.format("Invalid salary: %.2f. Must be ≥ %.2f", salary, MIN_SALARY)
            );
        }
    }

    public static int parseValidId(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    String.format("Invalid ID format: '%s'", value), e
            );
        }
    }

    public static void validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    String.format("Invalid ID: %d. Must be positive", id)
            );
        }
    }

    public static void validateNonEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("%s cannot be empty", fieldName)
            );
        }
    }

    public static void validateNameLength(String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("Name exceeds maximum length (%d): %s",
                            MAX_NAME_LENGTH, name)
            );
        }
    }

    public static void validateDepartmentName(String department) {
        for (String allowed : ALLOWED_DEPARTMENTS) {
            if (allowed.equalsIgnoreCase(department)) return;
        }
        throw new IllegalArgumentException(
                String.format("Invalid department: %s. Allowed: %s",
                        department, Arrays.toString(ALLOWED_DEPARTMENTS))
        );
    }

    public static double parseDouble(String value, String fieldName) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    String.format("Invalid %s format: '%s'", fieldName, value), e
            );
        }
    }
}