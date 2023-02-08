package com.resttutorials.payroll;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>{
    
    // Use the same interface as the EmployeeRepository
}
