package com.gillevgenii.employeeprocessor.utils;

public class ParsingUtils {
    public static int parseInteger(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректный формат идентификатора: '" + value + "'", e);
        }
    }

    public static double parseDouble(String value, String fieldName) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректный формат " + fieldName + ": '" + value + "'", e);
        }
    }
}

