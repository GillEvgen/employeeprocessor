package com.gillevgenii.employeeprocessor.service;

import com.gillevgenii.employeeprocessor.model.Department;
import com.gillevgenii.employeeprocessor.model.Employee;
import com.gillevgenii.employeeprocessor.model.Manager;
import com.gillevgenii.employeeprocessor.utils.ValidationUtils;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class DataParser {

    /**
     * Парсит строку данных и создает объект Employee или Manager.
     *
     * @param line строка с данными
     * @return объект Employee или Manager
     * @throws IllegalArgumentException если строка некорректна
     */
    public Employee parseLine(String line) {
        String[] parts = line.split(",");
        if (parts.length < 5) {
            throw new IllegalArgumentException("Некорректный формат данных: " + line);
        }

        String position = parts[0].trim();
        int id = ValidationUtils.parseInt(parts[1].trim(), "Некорректный идентификатор сотрудника");
        String name = parts[2].trim();
        double salary = ValidationUtils.parseDouble(parts[3].trim(), "Некорректная зарплата");
        if (!ValidationUtils.isValidSalary(salary)) {
            throw new IllegalArgumentException("Зарплата должна быть больше 0: " + salary);
        }

        String lastPart = parts[4].trim();
        if ("Manager".equalsIgnoreCase(position)) {
            // Парсинг менеджера
            return new Manager(position, id, name, salary, lastPart);
        } else if ("Employee".equalsIgnoreCase(position)) {
            // Парсинг сотрудника
            int managerId = ValidationUtils.parseInt(lastPart, "Некорректный идентификатор менеджера");
            return new Employee(position, id, name, salary, managerId);
        } else {
            throw new IllegalArgumentException("Некорректная должность: " + position);
        }
    }

    /**
     * Фильтрует сотрудников, исключая тех, у кого отсутствуют менеджеры.
     *
     * @param employees список сотрудников
     * @param managers  мапа менеджеров
     * @return список валидных сотрудников
     */
    public Set<Employee> filterValidEmployees(Set<Employee> employees, Map<Integer, Manager> managers) {
        return employees.stream()
                .filter(e -> managers.containsKey(e.getManagerId())) // У сотрудника должен быть менеджер
                .collect(Collectors.toSet());
    }

    /**
     * Группирует сотрудников по департаментам на основе их менеджеров.
     *
     * @param managers  мапа менеджеров
     * @param employees список валидных сотрудников
     * @return мапа департаментов
     */
    public Map<String, Department> groupByDepartments(Map<Integer, Manager> managers, Set<Employee> employees) {
        Map<String, Department> departments = new TreeMap<>(); // TreeMap для сортировки департаментов

        // Создаем департаменты для каждого менеджера
        for (Manager manager : managers.values()) {
            departments.put(manager.getDepartment(), new Department(manager.getDepartment(), manager));
        }

        // Добавляем сотрудников в соответствующие департаменты
        for (Employee employee : employees) {
            Manager manager = managers.get(employee.getManagerId());
            if (manager != null) {
                departments.get(manager.getDepartment()).addEmployee(employee);
            }
        }
        return departments;
    }
}
