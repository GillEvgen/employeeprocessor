package com.gillevgenii.employeeprocessor.service;

import com.gillevgenii.employeeprocessor.model.Department;
import com.gillevgenii.employeeprocessor.model.Employee;
import com.gillevgenii.employeeprocessor.model.Manager;

import java.util.*;
import java.util.stream.Collectors;

public class DataFilter {

    /**
     * Удаляет сотрудников без корректного менеджера.
     *
     * @param employees список сотрудников
     * @param managers  карта менеджеров (ID -> Manager)
     * @return отфильтрованный список сотрудников
     */
    public DataProcessingResult filterValidEmployees(Set<Employee> employees, Map<Integer, Manager> managers, List<String> invalidData) {
        Set<Employee> validEmployees = new HashSet<>();

        employees.forEach(employee -> {
            if (managers.containsKey(employee.getManagerId())) {
                validEmployees.add(employee);
            } else {
                invalidData.add(String.format("Employee,%d,%s,%.1f,%d | Ошибка: Отсутствует менеджер",
                        employee.getId(), employee.getName(), employee.getSalary(), employee.getManagerId()));
            }
        });

        return new DataProcessingResult(managers, validEmployees, invalidData);
    }

    /**
     * Группирует сотрудников и менеджеров по департаментам.
     *
     * @param managers  карта менеджеров (ID -> Manager)
     * @param employees список сотрудников
     * @return карта департаментов (название -> Department)
     */
    public Map<String, Department> groupByDepartments(Map<Integer, Manager> managers, Set<Employee> employees) {
        // Создаём департаменты
        Map<String, Department> departments = managers.values().stream()
                .collect(Collectors.toMap(
                        Manager::getDepartment,
                        manager -> new Department(manager.getDepartment(), manager),
                        (existing, replacement) -> existing
                ));

        // Добавляем сотрудников в департаменты
        employees.forEach(employee -> {
            Manager manager = managers.get(employee.getManagerId());
            if (manager != null) {
                Department department = departments.get(manager.getDepartment());
                if (department != null) {
                    department.addEmployee(employee);
                }
            }
        });

        return sortDepartments(departments);
    }

    /**
     * Сортирует департаменты: HR -> Sales -> остальные в лексикографическом порядке.
     *
     * @param departments карта департаментов
     * @return отсортированная карта департаментов
     */
    private Map<String, Department> sortDepartments(Map<String, Department> departments) {
        return departments.entrySet().stream()
                .sorted(getDepartmentComparator())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
    }

    /**
     * Компаратор для сортировки департаментов.
     * HR всегда первый, Sales второй, остальные сортируются по алфавиту.
     *
     * @return компаратор для сортировки департаментов
     */
    private Comparator<Map.Entry<String, Department>> getDepartmentComparator() {
        return Comparator.comparing((Map.Entry<String, Department> entry) -> {
            String dep = entry.getKey();
            if ("HR".equals(dep)) {
                return 0;
            }
            if ("Sales".equals(dep)) {
                return 1;
            }
            return 2;
        }).thenComparing(Map.Entry::getKey);
    }
}