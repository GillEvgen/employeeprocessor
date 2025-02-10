package com.gillevgenii.employeeprocessor.utils;

import java.util.Arrays;
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
                if (!arg.startsWith("--")) {
                    throw new IllegalArgumentException("Аргумент должен начинаться с '--': " + arg);
                }

                String[] parts = arg.substring(2).split("=", 2);
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Некорректный аргумент: " + arg);
                }

                String key = parts[0].trim().toLowerCase(); // Ключ
                String value = parts[1].trim(); // Значение
                arguments.put(key, value);
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
        boolean missingArgument = java.util.Arrays.stream(requiredKeys)
                .anyMatch(key -> !arguments.containsKey(key) || arguments.get(key).isBlank());

        if (missingArgument) {
            throw new IllegalArgumentException("Отсутствует обязательный аргумент: " +
                    java.util.Arrays.stream(requiredKeys)
                            .filter(key -> !arguments.containsKey(key) || arguments.get(key).isBlank())
                            .findFirst()
                            .orElse(""));
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
        String value = arguments.getOrDefault(key, "").toLowerCase();

        if (value.isEmpty()) {
            return; // Если ключ отсутствует, просто выходим
        }

        boolean isValid = Arrays.stream(validValues).anyMatch(value::equals);

        if (!isValid) {
            throw new IllegalArgumentException("Некорректное значение для --" + key + ": " + value);
        }
    }
}
