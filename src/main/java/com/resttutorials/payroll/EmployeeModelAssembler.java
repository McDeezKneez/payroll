package com.resttutorials.payroll;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class EmployeeModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>>{
    
    @Override
    public EntityModel<Employee> toModel(Employee employee) {

        Link selfLink;
        try {
            selfLink = linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel();
        } catch (NoSuchMethodException e) {
            selfLink = null;
            e.printStackTrace();
        } catch (SecurityException e) {
            selfLink = null;
            e.printStackTrace();
        }

        Link rootLink;
        rootLink = linkTo(methodOn(EmployeeController.class).all()).withRel("employees");

        return EntityModel.of(employee, selfLink, rootLink);
    }
}
