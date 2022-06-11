package com.example.complexnatatie.services;

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
        return CustomerDTO.fromCustomers(customerRepository.findAll());
    }

    public List<CustomerDTO> getByName(String name) {
        final String newName = name.toLowerCase().replace("%20", " ");

        return CustomerDTO.fromCustomers(customerRepository.getByName(newName));
    }

    public CustomerDTO save(CustomerDTO customerDTO) {
        Customer customer = Customer.fromCustomerDTO(customerDTO);
        customer = customerRepository.save(customer);
        return CustomerDTO.fromCustomer(customer);
    }

    public CustomerDTO update(int id, CustomerDTO customerDTO) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);

        if (optionalCustomer.isEmpty()) {
            LOGGER.error("Customer with id: {} was not found in database", id);
            throw new ResourceNotFoundException("Customer with id: " + id + " was not found in database");
        }

        Customer customer = optionalCustomer.get();

        final String phone = customerDTO.getPhone();
        if (phone != null && !phone.isEmpty()) {
            customer.setPhone(phone);
        }

        final String email = customerDTO.getEmail();
        if (email != null && !email.isEmpty()) {
            customer.setEmail(email);
        }

        customerRepository.save(customer);

        return CustomerDTO.fromCustomer(customer);
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
