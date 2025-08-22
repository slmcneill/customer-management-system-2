package com.seashells.backend;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
//import org.springframework.http.HttpMethod;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;

import com.seashells.backend.domain.Customer;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerAPITests {
    @Autowired
    com.seashells.backend.repository.CustomersRepository repo;

    @org.junit.jupiter.api.BeforeEach
    public void setup() {
        if (!repo.existsById(0L)) {
            Customer customer = new Customer();
            customer.setId(0L);
            customer.setName("TestUser");
            customer.setEmail("testuser@email.com");
            customer.setPassword("password");
            customer.setUserName("testuser");
            repo.save(customer);
        }
        if (!repo.existsById(2L)) {
            Customer customer = new Customer();
            customer.setId(2L);
            customer.setName("TestUser2");
            customer.setEmail("testuser2@email.com");
            customer.setPassword("password");
            customer.setUserName("testuser2");
            repo.save(customer);
        }
    }
    
    @Autowired TestRestTemplate template;
    
    private HttpHeaders getAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer testtoken"); // Accepts any non-empty token
        return headers;
    }

    @Test
    @Disabled
    public void testGetList() {
        HttpEntity<?> entity = new HttpEntity<>(getAuthHeaders());
        ResponseEntity<Customer[]> response = template.exchange("/api/customers", org.springframework.http.HttpMethod.GET, entity, Customer[].class);
        Customer[] customers = response.getBody();
        assertNotNull(customers);
        assertNotNull(customers[0]);
        assertNotNull(customers[0].getId());
        assertTrue(customers.length > 0);
    }
    @Test
    @Disabled
    public void testGet() {
        HttpEntity<?> entity = new HttpEntity<>(getAuthHeaders());
        ResponseEntity<Customer> response = template.exchange("/api/customers/{id}", org.springframework.http.HttpMethod.GET, entity, Customer.class, 0);
        Customer customer = response.getBody();
        assertNotNull(customer);
        assertNotNull(customer.getId());
    }



    @Test
    @Disabled
    public void testPost() {
        Customer customer = new Customer();
        customer.setName("Test");
        customer.setEmail("test@test.com");
    
        URI location = template.postForLocation("/api/customers", customer, Customer.class);
    
        assertNotNull(location, "Location header should not be null");
            Customer savedCustomer = template.getForObject(location, Customer.class);
            assertNotNull(savedCustomer, "Saved customer should not be null");
        assertNotNull(savedCustomer.getId(), "Saved customer ID should not be null");
        assertEquals("Test", savedCustomer.getName(), "Customer name should match");
        assertEquals("test@test.com", savedCustomer.getEmail(), "Customer email should match");
    }


    @Test
    @Disabled
    public void testPut() {
        String path = "/api/customers/2";
        String newValue = "NewValue" + Math.random();
        HttpEntity<?> entity = new HttpEntity<>(getAuthHeaders());
        ResponseEntity<Customer> response = template.exchange(path, org.springframework.http.HttpMethod.GET, entity, Customer.class);
        Customer customer = response.getBody();
        customer.setName(newValue);
        HttpEntity<Customer> putEntity = new HttpEntity<>(customer, getAuthHeaders());
        template.exchange(path, org.springframework.http.HttpMethod.PUT, putEntity, Void.class);
        response = template.exchange(path, org.springframework.http.HttpMethod.GET, entity, Customer.class);
        customer = response.getBody();
        assertEquals(newValue, customer.getName());
    }

}