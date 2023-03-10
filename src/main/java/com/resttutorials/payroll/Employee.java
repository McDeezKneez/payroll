package com.resttutorials.payroll;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

// JPA annotation to make this object ready for storage in a JPA-based data store
// Had to add the javax persistency dependency manually, see notes

// Turns out the newest version of springboot doesnt have support for
// javax but has support for jakarta
// Was getting errors when running before making the change to jakarta, "Employee not a managed type"
@Entity
class Employee {

    private @Id @GeneratedValue Long id;
    // Id - JPA annotation to indicate that it is the primary key
    // GeneratedValue - JPA annotation tells JPA to automatically generate this value
    private String name;
    private String role;

    Employee() {
    }

    Employee(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Employee)) {
            return false;
        }

        Employee employee = (Employee) o;
        return Objects.equals(this.id, employee.id) && Objects.equals(this.name, employee.name)
                && Objects.equals(this.role, employee.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.role);
    }

    @Override
    public String toString() {
        return "Employee{" + "id=" + this.id + ", name='" + this.name + '\'' + ", role=" + this.role + '\'' + '}';
    }
}
