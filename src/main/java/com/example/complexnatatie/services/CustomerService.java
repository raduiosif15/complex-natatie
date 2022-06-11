package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.CustomerBuilder;
import com.example.complexnatatie.controllers.handlers.exceptions.ResourceNotFoundException;
import com.example.complexnatatie.dtos.CustomerDTO;
import com.example.complexnatatie.entities.Customer;
import com.example.complexnatatie.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public record CustomerService(CustomerRepository customerRepository) {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    public List<CustomerDTO> getAll() {
        return CustomerBuilder.fromEntities(customerRepository.findAll());
    }

    public List<CustomerDTO> getByName(String name) {
        final String newName = name.toLowerCase().replace("%20", " ");

        return CustomerBuilder.fromEntities(customerRepository.getByName(newName));
    }

    public CustomerDTO save(CustomerDTO customerDTO) {
        Customer customer = CustomerBuilder.fromDTO(customerDTO);
        customer = customerRepository.save(customer);
        return CustomerBuilder.fromEntity(customer);
    }

    public CustomerDTO update(int id, CustomerDTO customerDTO) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);

        if (optionalCustomer.isEmpty()) {
            LOGGER.error("Customer with id: {} was not found in database", id);
            throw new ResourceNotFoundException("Customer with id: " + id + " was not found in database");
        }

        Customer customer = optionalCustomer.get();
        customer.setPhone(customerDTO.getPhone());
        customer.setEmail(customerDTO.getEmail());
        customer.setUtcnID(customerDTO.getUtcnID());
        customer.setCustomerType(customerDTO.getCustomerType().getName());

        customerRepository.save(customer);

        return CustomerBuilder.fromEntity(customer);
    }

    public CustomerDTO delete(int id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);

        if (optionalCustomer.isEmpty()) {
            LOGGER.error("Customer with id: {} was not found in database", id);
            throw new ResourceNotFoundException("Customer with id: " + id + " was not found in database");
        }

        customerRepository.delete(optionalCustomer.get());

        return null;
    }
}
