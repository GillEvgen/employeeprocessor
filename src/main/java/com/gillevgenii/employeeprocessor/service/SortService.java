package com.gillevgenii.employeeprocessor.service;

import com.gillevgenii.employeeprocessor.model.Department;
import com.gillevgenii.employeeprocessor.model.Employee;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SortService {

    /**
     * Сортирует сотрудников внутри каждого департамента.
     *
     * @param departments мапа департаментов
     * @param sortBy      критерий сортировки ("name" или "salary")
     * @param order       порядок сортировки ("asc" или "desc")
     */
    public void sortDepartments(Map<String, Department> departments, String sortBy, String order) {
        if (!isValidSortBy(sortBy)) {
            throw new IllegalArgumentException("Некорректный параметр сортировки: " + sortBy);
        }

        if (!isValidOrder(order)) {
            throw new IllegalArgumentException("Некорректный порядок сортировки: " + order);
        }

        Comparator<Employee> comparator = getComparator(sortBy, order);

        // Проходим по всем департаментам и сортируем сотрудников
        for (Department department : departments.values()) {
            Set<Employee> sortedEmployees = department.getEmployees()
                    .stream()
                    .sorted(comparator) // Применяем сортировку
                    .collect(Collectors.toCollection(LinkedHashSet::new)); // Сохраняем порядок в LinkedHashSet

            // Обновляем сотрудников в департаменте с отсортированным множеством
            department.getEmployees().clear();
            department.getEmployees().addAll(sortedEmployees);
        }
    }

    /**
     * Возвращает компаратор для сортировки сотрудников.
     *
     * @param sortBy критерий сортировки ("name" или "salary")
     * @param order  порядок сортировки ("asc" или "desc")
     * @return компаратор для сортировки
     */
    private Comparator<Employee> getComparator(String sortBy, String order) {
        Comparator<Employee> comparator;

        if ("name".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(Employee::getName);
        } else { // sortBy = "salary"
            comparator = Comparator.comparing(Employee::getSalary);
        }

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        return comparator;
    }

    /**
     * Проверяет валидность параметра сортировки.
     *
     * @param sortBy критерий сортировки
     * @return true, если параметр валиден
     */
    private boolean isValidSortBy(String sortBy) {
        return "name".equalsIgnoreCase(sortBy) || "salary".equalsIgnoreCase(sortBy);
    }

    /**
     * Проверяет валидность параметра порядка сортировки.
     *
     * @param order порядок сортировки
     * @return true, если порядок валиден
     */
    private boolean isValidOrder(String order) {
        return "asc".equalsIgnoreCase(order) || "desc".equalsIgnoreCase(order);
    }
}
