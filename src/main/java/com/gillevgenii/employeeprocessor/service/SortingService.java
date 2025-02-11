package com.gillevgenii.employeeprocessor.service;

import com.gillevgenii.employeeprocessor.model.Department;
import com.gillevgenii.employeeprocessor.model.Employee;

import java.util.*;
import java.util.stream.Collectors;

public class SortingService {

    public void sortDepartments(Map<String, Department> departments, String sortBy, String order) {
        Comparator<Employee> comparator = getComparator(sortBy, order);

        for (Department department : departments.values()) {
            Set<Employee> sortedEmployees = department.getEmployees()
                    .stream()
                    .sorted(comparator)
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            department.getEmployees().clear();
            department.getEmployees().addAll(sortedEmployees);
        }
    }

    private Comparator<Employee> getComparator(String sortBy, String order) {
        Comparator<Employee> comparator = "name".equalsIgnoreCase(sortBy)
                ? Comparator.comparing(Employee::getName)
                : Comparator.comparing(Employee::getSalary);

        return "desc".equalsIgnoreCase(order) ? comparator.reversed() : comparator;
    }
}
