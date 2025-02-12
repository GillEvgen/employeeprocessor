package com.gillevgenii.employeeprocessor.filter;

import com.gillevgenii.employeeprocessor.model.Employee;
import com.gillevgenii.employeeprocessor.model.Manager;
import com.gillevgenii.employeeprocessor.service.DataProcessingResult;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
        try {
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
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при фильтрации сотрудников: " + e.getMessage(), e);
        }
    }
}
