package com.resttutorials.payroll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// meta annotation that pulls in component scanning, auto configuration
// and property support.
// Fires up a servlet container and serves up our service
public class PayrollApplication {

	public static void main(String[] args) {
		SpringApplication.run(PayrollApplication.class, args);
	}

}
