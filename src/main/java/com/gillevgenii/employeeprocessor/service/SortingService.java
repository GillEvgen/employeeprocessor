package com.gillevgenii.employeeprocessor.service;

import com.gillevgenii.employeeprocessor.model.Department;
import com.gillevgenii.employeeprocessor.model.Employee;

import java.util.*;
import java.util.stream.Collectors;

public class SortingService {

    public static void sortDepartments(Map<String, Department> departments, String sortBy, String order) {
        try {
            Comparator<Employee> comparator = getComparator(sortBy, order);

            for (Department department : departments.values()) {
                Set<Employee> sortedEmployees = department.getEmployees()
                        .stream()
                        .sorted(comparator)
                        .collect(Collectors.toCollection(TreeSet::new));

                department.getEmployees().clear();
                department.getEmployees().addAll(sortedEmployees);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при сортировке департаментов: " + e.getMessage(), e);
        }
    }

    private static Comparator<Employee> getComparator(String sortBy, String order) {
        try {
            Comparator<Employee> comparator = "name".equalsIgnoreCase(sortBy)
                    ? Comparator.comparing(Employee::getName)
                    : Comparator.comparing(Employee::getSalary);

            return "desc".equalsIgnoreCase(order) ? comparator.reversed() : comparator;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении компаратора сортировки: " + e.getMessage(), e);
        }
    }
}
