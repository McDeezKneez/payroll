package com.resttutorials.payroll;

import org.springframework.data.jpa.repository.JpaRepository;

interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Spring simplified this so once we declare this class as a child of JpaRepository we can now:
    // - Create, update, delete, and find Employees
    // Domain type = Employee, ID type = Long
    // We dont have to worry about the specifics of the data store implementation
}