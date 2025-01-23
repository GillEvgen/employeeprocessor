package com.gillevgenii.employeeprocessor.service;

import com.gillevgenii.employeeprocessor.model.Department;
import com.gillevgenii.employeeprocessor.model.Employee;
import com.gillevgenii.employeeprocessor.model.Manager;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DataFilter {

    /**
     * Удаляет сотрудников без корректного менеджера.
     *
     * @param employees список сотрудников
     * @param managers  карта менеджеров (ID -> Manager)
     * @return отфильтрованный список сотрудников
     */
    public Set<Employee> filterValidEmployees(Set<Employee> employees, Map<Integer, Manager> managers) {
        return employees.stream()
                .filter(employee -> managers.containsKey(employee.getManagerId()))
                .collect(Collectors.toSet());
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
        employees.stream().forEach(employee -> {
            Manager manager = managers.get(employee.getManagerId());
            if (manager != null) {
                Department department = departments.get(manager.getDepartment());
                if (department != null) {
                    department.addEmployee(employee);
                }
            }
        });
        return departments;
    }
}
