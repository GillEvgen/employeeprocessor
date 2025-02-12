package com.gillevgenii.employeeprocessor.grouping;

import com.gillevgenii.employeeprocessor.model.Department;
import com.gillevgenii.employeeprocessor.model.Employee;
import com.gillevgenii.employeeprocessor.model.Manager;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DepartmentGrouping {

    /**
     * Группирует сотрудников и менеджеров по департаментам.
     *
     * @param managers  карта менеджеров (ID -> Manager)
     * @param employees список сотрудников
     * @return карта департаментов (название -> Department)
     */
    public static Map<String, Department> groupByDepartments(Map<Integer, Manager> managers, Set<Employee> employees) {
        try {
            Map<String, Department> departments = managers.values().stream()
                    .collect(Collectors.toMap(
                            Manager::getDepartment,
                            manager -> new Department(manager.getDepartment(), manager),
                            (existing, replacement) -> existing,
                            LinkedHashMap::new
                    ));

            employees.forEach(employee -> {
                Manager manager = managers.get(employee.getManagerId());
                if (manager != null) {
                    Department department = departments.get(manager.getDepartment());
                    if (department != null) {
                        department.addEmployee(employee);
                    }
                }
            });

            return departments;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при группировке департаментов: " + e.getMessage(), e);
        }
    }
}
