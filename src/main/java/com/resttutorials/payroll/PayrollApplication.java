package com.resttutorials.payroll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Following guide: https://spring.io/guides/tutorials/rest/

// The spring inversion of control (IoC) container is at the core of the spring framework.
// it will create the objects, wire them together, configure them and
// manage their complete lifecycle from creation till destruction.
// Spring container uses dependency injection to manage the components that make up 
// an application.
// Dependency injection (DI) is the concept in which objects get other required objects from outside
// Spring IoC container uses metadata provided via XML, java annotations or the java code to produce
// a fully configured and executable system or application

// A bean is an object that is managed by the spring IoC container.
// Bean definition contains information called configuration metadata which is
// needed to know - how to create a bean - beans lifecycle details - beans dependencies
// Properties and descriptions that make up each bean definition:
// class - mandatory - specifies the bean class to be used to create the bean
// name - specifies the bean identifier uniquely
// scope - specifies the scope of the objects created from a particular bean definition
//   singleton - a single instance per spring IoC container (default)
//   prototype - sign bean definition to have any number of object instances
//   request - bean definition to an HTTP request
//   session - bean definition to an HTTP session
//   global-session - bean definition to an global HTTP session
// constructor-arg - 
// properties - 
// autowiring mode - 
// lazy-initialization mode - tells the IoC container to create a bean instance when it is first
//   requested rather than at startup
// initialization method - 
// destruction method - 


@SpringBootApplication
// meta annotation that pulls in component scanning, auto configuration
// and property support.
// Fires up a servlet container and serves up our service
public class PayrollApplication {

	public static void main(String... args) {
		SpringApplication.run(PayrollApplication.class, args);
	}

}
