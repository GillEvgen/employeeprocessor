package com.gillevgenii.employeeprocessor.model;

import java.util.Objects;

public class Employee{

    private final String position;
        private final int id;
        private final String name;
        private final double salary;
        private final int managerId;

    public Employee(String position, int id, String name, double salary, int managerId) {
            this.position = position;
            this.id = id;
            this.name = name.trim();
            this.salary = salary;
            this.managerId = managerId;
        }

        public String getPosition() {
            return position;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public double getSalary() {
            return salary;
        }

        public int getManagerId() {
            return managerId;
        }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id && Double.compare(employee.salary, salary) == 0 && managerId == employee.managerId && Objects.equals(position, employee.position) && Objects.equals(name, employee.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, id, name, salary, managerId);
    }

    @Override
        public String toString() {
            return String.format("%s,%d,%s,%.2f,%d", position, id, name, salary, managerId);
        }
}
