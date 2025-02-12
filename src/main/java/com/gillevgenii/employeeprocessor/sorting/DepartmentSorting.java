package com.gillevgenii.employeeprocessor.sorting;

import com.gillevgenii.employeeprocessor.model.Department;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DepartmentSorting {

    /**
     * Сортирует департаменты: HR -> Sales -> остальные в лексикографическом порядке.
     *
     * @param departments карта департаментов
     * @return отсортированная карта департаментов
     */
    public static Map<String, Department> sortDepartments(Map<String, Department> departments) {
        try {
            return departments.entrySet().stream()
                    .sorted(getDepartmentComparator())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (existing, replacement) -> existing,
                            LinkedHashMap::new
                    ));
        } catch (Exception e) {
            throw new RuntimeException("Не удалось отсортировать департаменты", e);
        }
    }

    /**
     * Компаратор для сортировки департаментов.
     * HR всегда первый, Sales второй, остальные сортируются по алфавиту.
     *
     * @return компаратор для сортировки департаментов
     */
    private static Comparator<Map.Entry<String, Department>> getDepartmentComparator() {
        return Comparator.comparing((Map.Entry<String, Department> entry) -> {
            try {
                String dep = entry.getKey();
                if ("HR".equals(dep)) {
                    return 0;
                } else if ("Sales".equals(dep)) {
                    return 1;
                }
                return 2;
            } catch (Exception e) {
                throw new RuntimeException("Ошибка в компараторе при обработке департамента: " + entry.getKey(), e);
            }
        }).thenComparing(Map.Entry::getKey);
    }
}