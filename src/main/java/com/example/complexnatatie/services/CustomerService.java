package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.CustomerBuilder;
import com.example.complexnatatie.controllers.handlers.exceptions.ResourceNotFoundException;
import com.example.complexnatatie.dtos.CustomerDTO;
import com.example.complexnatatie.entities.Customer;
import com.example.complexnatatie.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public record CustomerService(CustomerRepository customerRepository) {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    public List<CustomerDTO> getAll() {
        return CustomerBuilder.fromEntities(customerRepository.findAll());
    }

    public List<CustomerDTO> getByNameOrCodeID(String name) {

        try {

            final long codeID = Long.parseLong(name);
            final List<CustomerDTO> list = new ArrayList<>();

            if (codeID == 0) {
                return list;
            }

            final Optional<Customer> optionalCustomer = customerRepository.getByCodeID(codeID);

            if (optionalCustomer.isEmpty()) {
                return list;
            }

            list.add(CustomerBuilder.fromEntity(optionalCustomer.get()));

            return list;

        } catch (NumberFormatException e) {

            final String newName = name.toLowerCase().replace("%20", " ");

            return CustomerBuilder.fromEntities(customerRepository.getByName(newName));

        }

    }

    public CustomerDTO save(CustomerDTO customerDTO) {
        // todo: check if customer already exists (by utcnID or codeID)
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
        customer.setUtcnID(customerDTO.getUtcnID());
        customer.setType(customerDTO.getType().getName());

        customerRepository.save(customer);

        return CustomerBuilder.fromEntity(customer);
    }

    public Object deleteById(int id) {
        customerRepository.deleteById(id);
        return null;
    }
}
