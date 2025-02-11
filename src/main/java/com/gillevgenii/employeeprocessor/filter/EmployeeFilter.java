package com.gillevgenii.employeeprocessor.filter;

import com.gillevgenii.employeeprocessor.model.Department;
import com.gillevgenii.employeeprocessor.model.Employee;
import com.gillevgenii.employeeprocessor.model.Manager;
import com.gillevgenii.employeeprocessor.service.DataProcessingResult;

import java.util.*;
import java.util.stream.Collectors;

public class EmployeeFilter {

    /**
     * Удаляет сотрудников без корректного менеджера.
     *
     * @param employees список сотрудников
     * @param managers  карта менеджеров (ID -> Manager)
     * @return отфильтрованный список сотрудников
     */

    public static DataProcessingResult filterValidEmployees(Set<Employee> employees, Map<Integer, Manager> managers, List<String> invalidData) {
        Set<Employee> validEmployees = employees.stream()
                .filter(employee -> {
                    boolean hasManager = managers.containsKey(employee.getManagerId());
                    if (!hasManager) {
                        invalidData.add(String.format("Employee,%d,%s,%.1f,%d | Ошибка: Отсутствует менеджер",
                                employee.getId(), employee.getName(), employee.getSalary(), employee.getManagerId()));
                    }
                    return hasManager;
                })
                .collect(Collectors.toSet());

        return new DataProcessingResult(managers, validEmployees, invalidData);
    }
}






