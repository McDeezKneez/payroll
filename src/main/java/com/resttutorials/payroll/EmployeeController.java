package com.resttutorials.payroll;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
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
    private final EmployeeModelAssembler assembler;

    EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    // This method represents the employees aggregate root
    @GetMapping("/employees")
    // CollectionModel spring hateoas container aimed at encapsulating a collection
    // of resources
    CollectionModel<EntityModel<Employee>> all() {

        List<EntityModel<Employee>> employees = repository.findAll().stream().map(assembler::toModel)
                .collect(Collectors.toList());

        // Above map() call is functional programming. The List is turned into a stream and we run each value
        // through the toModel() method and then collect the values and turn it into a list.

        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());

        // return repository.findAll();
    }

    @PostMapping("/employees")
    ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) {

        EntityModel<Employee> entityModel = assembler.toModel(repository.save(newEmployee));

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        // Very unreadable code^
        // ResponseEntity is used to create an HTTP 2001 Created status message.
        // This response usually includes a Location header.
        // We use the URI derived from the model's self related link
        // return repository.save(newEmployee);
    }

    @GetMapping("/employees/{id}")
    // EntityModel is a generic container from spring HATEOAS that includes
    // not only the data but a collection of links
    // Had to add public because the getMethod call will only find public methods
    public EntityModel<Employee> one(@PathVariable Long id) throws NoSuchMethodException, SecurityException {
        Employee employee = repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));

        // Method self = EmployeeController.class.getMethod("one", Long.class);

        // Below doesnt work
        // Method self = EmployeeController.class.getEnclosingMethod();

        // for some reason, static imports are not working and I have to fully qualify
        // the calls
        // -- Had to modify the settings.json favorite static import section to include
        // -- the WebMvcLinkBuilder package.
        // return EntityModel.of(employee,
        // linkTo(self, id).withSelfRel(),
        // linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));

        /*
         * return EntityModel.of(employee,
         * linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
         * // ^This line asks spring hateoas to build a link to the EmployeeControllers
         * // one() method and flag it as a self link
         * linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
         * // ^This line asks spring hateoas to build a link to the aggregate root
         * // all() and call it employees
         */

        return assembler.toModel(employee);
    }

    @PutMapping("/employees/{id}")
    ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

        Employee updatedEmployee = repository.findById(id).map(employee -> {
            employee.setName(newEmployee.getName());
            employee.setRole(newEmployee.getRole());
            return repository.save(employee);
        }).orElseGet(() -> {
            newEmployee.setId(id);
            return repository.save(newEmployee);
        });

        // The Employee object returned by the save() is the one stored into the updatedEmployee var.
        // This object needs to be converted into a ResponseEntity<Employee> object.
        // First the Employee must be converted into a EntityModel, which will create all the proper
        // Links.
        EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);

        // Now that we have an EntityModel with the proper Links, we need to return a proper ResponseEntity.
        // the created() method creates a reponse that tells us a new entry has been created. We arent really
        // "creating" a new entity but we can use this ResponseEntity since it has the same structure.
        // The created() method needs a URI of the entity created so we used the getRequiredLink method to
        // grab the appropriate self link within the EntityModel and then convert it to a URI with toUri().
        // The body() method populates the body of the newly created ResponseEntity with the entire entityModel.
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);

        // return repository.findById(id).map(employee -> {
        // employee.setName(newEmployee.getName());
        // employee.setRole(newEmployee.getRole());
        // return repository.save(employee);
        // }).orElseGet(() -> {
        // newEmployee.setId(id);
        // return repository.save(newEmployee);
        // });
    }

    @DeleteMapping("/employees/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
