package com.gillevgenii.employeeprocessor.service;

import com.gillevgenii.employeeprocessor.model.Employee;
import com.gillevgenii.employeeprocessor.model.Manager;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Класс для хранения промежуточных результатов обработки данных.
 */
public class DataProcessingResult {

    private final Map<Integer, Manager> managers;
    private final Set<Employee> validEmployees;
    private final List<String> invalidData;

    /**
     * Конструктор для инициализации всех полей.
     *
     * @param managers       карта менеджеров
     * @param validEmployees набор валидных сотрудников
     * @param invalidData    список некорректных данных
     */
    public DataProcessingResult(Map<Integer, Manager> managers, Set<Employee> validEmployees, List<String> invalidData) {
        this.managers = managers;
        this.validEmployees = validEmployees;
        this.invalidData = invalidData;
    }

    /**
     * Возвращает карту менеджеров.
     *
     * @return карта менеджеров
     */
    public Map<Integer, Manager> getManagers() {
        return managers;
    }

    /**
     * Возвращает набор валидных сотрудников.
     *
     * @return набор сотрудников
     */
    public Set<Employee> getValidEmployees() {
        return validEmployees;
    }

    /**
     * Возвращает список некорректных данных.
     *
     * @return список строк с некорректными данными
     */
    public List<String> getInvalidData() {
        return invalidData;
    }
}
