package com.gillevgenii.employeeprocessor.utils;

import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {

    /**
     * Парсит аргументы командной строки и возвращает их в виде мапы.
     *
     * @param args массив аргументов командной строки
     * @return мапа аргументов (ключ -> значение)
     * @throws IllegalArgumentException если аргументы некорректны
     */
    public static Map<String, String> parse(String[] args) {
        Map<String, String> arguments = new HashMap<>();

        for (String arg : args) {
            if (arg.startsWith("--")) {
                String[] parts = arg.substring(2).split("=", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim().toLowerCase(); // Ключ
                    String value = parts[1].trim(); // Значение
                    arguments.put(key, value);
                } else {
                    throw new IllegalArgumentException("Некорректный аргумент: " + arg);
                }
            } else {
                throw new IllegalArgumentException("Аргумент должен начинаться с '--': " + arg);
            }
        }

        return arguments;
    }

    /**
     * Проверяет наличие обязательных аргументов.
     *
     * @param arguments мапа аргументов
     * @param requiredKeys список обязательных аргументов
     * @throws IllegalArgumentException если отсутствует обязательный аргумент
     */
        public static void validateRequiredArguments(Map<String, String> arguments, String... requiredKeys) {
        for (String key : requiredKeys) {
            if (!arguments.containsKey(key) || arguments.get(key).isBlank()) {
                throw new IllegalArgumentException("Отсутствует обязательный аргумент: --" + key);
            }
        }
    }

    /**
     * Проверяет, что значение аргумента входит в допустимый список.
     *
     * @param arguments мапа аргументов
     * @param key ключ аргумента
     * @param validValues массив допустимых значений
     * @throws IllegalArgumentException если значение аргумента недопустимо
     */
    public static void validateArgumentValue(Map<String, String> arguments, String key, String... validValues) {
        if (arguments.containsKey(key)) {
            String value = arguments.get(key).toLowerCase();
            for (String validValue : validValues) {
                if (validValue.equals(value)) {
                    return;
                }
            }
            throw new IllegalArgumentException("Некорректное значение для --" + key + ": " + value);
        }
    }
}
