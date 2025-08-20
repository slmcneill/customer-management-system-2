package com.seashells.backend.api;

import java.net.URI;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.seashells.backend.domain.Customer;
import com.seashells.backend.repository.CustomersRepository;

// PART 3: Annotate this class to make it a Spring MVC controller for REST requests.
// Configure this class to handle mappings for all requests beginning with "/customers:"
@RestController
@RequestMapping("/api/customers")

public class CustomerAPI {
       // PART 4: 
    //  Define a variable named repo of type CustomerRepository.
    //  Have Spring Autowire it.
    private final CustomersRepository repo;
    public CustomerAPI(CustomersRepository repo) {
        this.repo = repo;
    }




    // PART 5: 
    //  Create a public method named "getAll" that takes no parameters.
    //  Have it return an Iterable of Customer objects.
    //  Add an annotation to have the method respond the GET requests.
    @GetMapping
    public Iterable<Customer> getAll() {
        return repo.findAll();
    }


    // PART 9:
    //  Create a public method named getCustomer returning Optional<Customer>.
    //  Annotate the method to respond to GET requests for the "/{id}" path.
    //  The method should take a long parameter named id annotated with @PathVariable.
    //  Add code to the method to return the value from the repository's findById method.


    @GetMapping("/{id}")
    public Optional<Customer> getCustomer(@PathVariable long id) {
        return repo.findById(id);
    }

    //  PART 13:
    //  Create a method called "addCustomer" returning ResponseEntity<?>.
    //  Annotate the method to respond to POST requests.
    //  The method should take a parameter of type Customer annotated with @RequestBody.
    //  Return a ResponseEntity.badRequest().build() if the incoming customer is missing a name or email.
    //  Otherwise, save the customer to the repository.
    //  Use ServletUriComponentsBuilder to construct a proper location URI.
    //  Return a ResponseEntity.created(uri).build()


    @PostMapping
    public ResponseEntity<?> addCustomer(@RequestBody Customer newCustomer) {
// Validate input:
        if ( newCustomer.getName()==null
                || newCustomer.getEmail() == null) {
            return ResponseEntity.badRequest().build();
        }
        newCustomer=repo.save(newCustomer);
// Location header will resemble
// "http://localhost:8080/customers/27"
        URI location =
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(newCustomer.getId())
                        .toUri();
        return ResponseEntity.created(location).build();
    }


    //  PART 16:
    //  Create a method called "putCustomer" returning ResponseEntity<?>.
    //  Annotate the method to respond to PUT requests for the "/{id}" path.
    //  The method should take a parameter of type Customer annotated with @RequestBody.
    //  Also define a long parameter named id annotated with @PathVariable.
    //  Check if the incoming customer's ID matches the path variable, and that email and name are not null.
    //  If not, return a ResponseEntity.badRequest().build().
    //  Otherwise, save the customer to the repository.
    //  Return a ResponseEntity.ok().build()
    @PutMapping("/{id}")
    public ResponseEntity<?> putCustomer(
            @RequestBody Customer customer,
            @PathVariable long id) {
        if (customer.getId()!=id
                || customer.getName()==null
                || customer.getEmail() == null) {
            return ResponseEntity.badRequest().build();
        }
        repo.save(customer);
        return ResponseEntity.ok().build();
    }


}
