package com.gillevgenii.employeeprocessor.validator;

import java.util.Arrays;
import java.util.Map;

public class ArgumentValidator {

    public static void validate(Map<String, String> arguments) {
        validateRequiredArguments(arguments, "path");
        validateArgumentValue(arguments, "s", "name", "salary");
        validateArgumentValue(arguments, "o", "console", "file");
    }

    private static void validateRequiredArguments(Map<String, String> arguments, String... requiredKeys) {
        Arrays.stream(requiredKeys)
                .filter(key -> !arguments.containsKey(key) || arguments.get(key).isBlank())
                .findFirst()
                .ifPresent(missingKey -> {
                    throw new IllegalArgumentException("Отсутствует обязательный аргумент: --" + missingKey);
                });
    }

    private static void validateArgumentValue(Map<String, String> arguments, String key, String... validValues) {
        String value = arguments.getOrDefault(key, "").toLowerCase();
        if (!value.isEmpty() && Arrays.stream(validValues).noneMatch(value::equals)) {
            throw new IllegalArgumentException("Некорректное значение для --" + key + ": " + value);
        }
    }
}
