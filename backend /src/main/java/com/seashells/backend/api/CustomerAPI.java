package com.seashells.backend.api;

import java.net.URI;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@CrossOrigin(origins = "*") 

public class CustomerAPI {
       // PART 4: 
    //  Define a variable named repo of type CustomerRepository.
    //  Have Spring Autowire it.
    private final CustomersRepository repo;
    public CustomerAPI(CustomersRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public Iterable<Customer> getAll() {
        return repo.findAll();
    }


    @GetMapping("/{id}")
    public Optional<Customer> getCustomer(@PathVariable long id) {
        return repo.findById(id);
    }


    @PostMapping
    public ResponseEntity<?> addCustomer(@RequestBody Customer newCustomer) {
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
        if (customer.getId() == null
                || customer.getId() != id
                || customer.getName() == null
                || customer.getEmail() == null) {
            return ResponseEntity.badRequest().build();
        }
        repo.save(customer);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
