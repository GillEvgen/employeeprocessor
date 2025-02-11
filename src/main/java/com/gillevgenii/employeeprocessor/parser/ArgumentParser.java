package com.gillevgenii.employeeprocessor.parser;

import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {

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

            arguments.put(parts[0].trim().toLowerCase(), parts[1].trim());
        }
        return arguments;
    }
}
