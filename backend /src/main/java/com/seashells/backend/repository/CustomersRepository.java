package com.seashells.backend.repository;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.seashells.backend.domain.Customer;
public interface CustomersRepository extends CrudRepository<Customer, Long>
{
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByUsername(String username);
}