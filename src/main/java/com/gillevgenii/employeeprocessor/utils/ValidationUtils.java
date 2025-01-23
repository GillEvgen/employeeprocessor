package com.gillevgenii.employeeprocessor.utils;

public class ValidationUtils {

    /**
     * Парсит строку в целое число.
     *
     * @param value строка для парсинга
     * @param errorMessage сообщение об ошибке, если парсинг не удался
     * @return распарсенное значение
     * @throws IllegalArgumentException если парсинг не удался
     */
    public static int parseInt(String value, String errorMessage) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorMessage + ": " + value);
        }
    }

    /**
     * Парсит строку в вещественное число.
     *
     * @param value строка для парсинга
     * @param errorMessage сообщение об ошибке, если парсинг не удался
     * @return распарсенное значение
     * @throws IllegalArgumentException если парсинг не удался
     */
    public static double parseDouble(String value, String errorMessage) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorMessage + ": " + value);
        }
    }

    /**
     * Проверяет, что зарплата корректна (больше 0).
     *
     * @param salary значение зарплаты
     * @return true, если зарплата корректна
     */
    public static boolean isValidSalary(double salary) {
        return salary > 0;
    }
}
