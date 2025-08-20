package com.seashells.accountservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/account")
public class AccountRootController {
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
        // TODO: Validate user against Customer API, check password, generate JWT
        if (loginRequest.username == null || loginRequest.password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing username or password");
        }
        // Placeholder response
        String fakeJwt = "FAKE.JWT.TOKEN";
        return ResponseEntity.ok().body(fakeJwt);
    }

    // Register endpoint stub
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        // TODO: Call Customer API to add new customer
        if (registerRequest.name == null || registerRequest.email == null || registerRequest.password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing registration fields");
        }
        // Placeholder response
        return ResponseEntity.ok().body("Registration successful (stub)");
    }
}
