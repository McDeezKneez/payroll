package com.resttutorials.payroll;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

// Spring Model-view-controller framework (MVC)
// MODEL - contains the data of the application, single object or collection
// CONTROLLER - contains the business logic of an application (this class)
// VIEW - represents the provided information in a particular format
// FRONT CONTROLLER - responsible for the flow of the spring mvc application
//   In spring web mvc, the DispatcherServlet class works as front controler

@RestController
class EmployeeController {

    private final EmployeeRepository repository;

    EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/employees")
    List<Employee> all() {
        return repository.findAll();
    }

    @PostMapping("/employees")
    Employee newEmployee(@RequestBody Employee newEmployee) {
        return repository.save(newEmployee);
    }

    @GetMapping("/employees/{id}")
    // EntityModel is a generic container from spring HATEOAS that includes
    // not only the data but a collection of links
    EntityModel<Employee> one(@PathVariable Long id) throws NoSuchMethodException, SecurityException {
        Employee employee = repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));

        Method self = EmployeeController.class.getMethod("one", Long.class);

        // for some reason, static imports are not working and I have to fully qualify
        // the calls
        // -- Had to modify the settings.json favorite static import section to include
        // -- the WebMvcLinkBuilder package.
        return EntityModel.of(employee,
                linkTo(self, id).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));

        /*
         * return EntityModel.of(employee,
         * linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
         * // ^This line asks spring hateoas to build a link to the EmployeeControllers
         * // one() method and flag it as a self link
         * linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
         * // ^This line asks spring hateoas to build a link to the aggregate root
         * // all() and call it employees
         */
    }

    @PutMapping("/employees/{id}")
    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
        return repository.findById(id).map(employee -> {
            employee.setName(newEmployee.getName());
            employee.setRole(newEmployee.getRole());
            return repository.save(employee);
        }).orElseGet(() -> {
            newEmployee.setId(id);
            return repository.save(newEmployee);
        });
    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
