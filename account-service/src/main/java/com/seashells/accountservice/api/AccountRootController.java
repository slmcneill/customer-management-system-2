package com.seashells.accountservice.api;

import com.seashells.accountservice.security.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/account")
public class AccountRootController {
    private final RestTemplate restTemplate;
    private final TokenService tokenService;

    // Inject RestTemplate and TokenService
    public AccountRootController(RestTemplate restTemplate, TokenService tokenService) {
        this.restTemplate = restTemplate;
        this.tokenService = tokenService;
    }

    // URL of CustomerAPI (adjust host/port as needed)
    private final String CUSTOMER_API_URL = "http://localhost:8080/api/customers";

    @GetMapping
    public String root() {
        return "Account Service is up and running.";
    }

    // DTO for login
    public static class LoginRequest {
        public String username;
        public String password;
    }

    // DTO for registration
    public static class RegisterRequest {
        public String name;
        public String email;
        public String password;
    }

    // Token endpoint stub
    @PostMapping("/token")
    public ResponseEntity<?> token(@RequestBody LoginRequest loginRequest) {
        if (loginRequest.username == null || loginRequest.password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing username or password");
        }

        // Call CustomerAPI to get all customers
        Customer[] customers = restTemplate.getForObject(CUSTOMER_API_URL, Customer[].class);
        if (customers == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Customer service unavailable");
        }

        for (Customer customer : customers) {
            if (customer.getEmail().equals(loginRequest.username) &&
                customer.getPassword().equals(loginRequest.password)) {
                // Generate JWT using TokenService
                // Create a simple Authentication object for TokenService
                org.springframework.security.core.Authentication auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                    loginRequest.username, loginRequest.password,
                    java.util.Collections.emptyList()
                );
                String jwt = tokenService.generateToken(auth);
                return ResponseEntity.ok().body(jwt);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    // Register endpoint stub
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        if (registerRequest.name == null || registerRequest.email == null || registerRequest.password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing registration fields");
        }

        // Create new customer object
        Customer newCustomer = new Customer(registerRequest.name, registerRequest.email, registerRequest.password);

        // Post to CustomerAPI
        ResponseEntity<?> response = restTemplate.postForEntity(CUSTOMER_API_URL, newCustomer, Object.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok().body("Registration successful");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
        }
    }

    // DTO for Customer (minimal)
    public static class Customer {
        private String name;
        private String email;
        private String password;

        public Customer() {}
        public Customer(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
        }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public void setName(String name) { this.name = name; }
        public void setEmail(String email) { this.email = email; }
        public void setPassword(String password) { this.password = password; }
    }
}
