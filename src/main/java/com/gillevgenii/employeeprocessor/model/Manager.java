package com.gillevgenii.employeeprocessor.model;

import java.util.Objects;

public class Manager extends Employee {

    private final String department;

    public Manager(String position, int id, String name, double salary, String department) {
        super(position, id, name, salary, 0);
        this.department = department.trim();
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Manager manager = (Manager) o;
        return Objects.equals(department, manager.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), department);
    }

    @Override
    public String toString() {
        return String.format("%s,%d,%s,%.2f,%s", getPosition(), getId(), getName(), getSalary(), department);
    }
}

