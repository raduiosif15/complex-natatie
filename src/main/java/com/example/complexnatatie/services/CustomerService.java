package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.CustomerBuilder;
import com.example.complexnatatie.controllers.handlers.exceptions.CustomException;
import com.example.complexnatatie.controllers.handlers.exceptions.ResourceNotFoundException;
import com.example.complexnatatie.dtos.CustomerDTO;
import com.example.complexnatatie.entities.Customer;
import com.example.complexnatatie.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
        if (customerDTO.getCodeID() > 0) {
            final List<CustomerDTO> list = getByNameOrCodeID(String.valueOf(customerDTO.getCodeID()));

            // if list is not empty there is just one customer with same codeID
            if (!list.isEmpty()) {
                LOGGER.error("Customer with code id: {} already exist in database", customerDTO.getCodeID());
                throw new CustomException(
                        "Clientul cu id-ul legitimatiei " + customerDTO.getCodeID() + " exista deja salvat in sistem",
                        HttpStatus.CONFLICT
                );
            }
        }

        if (customerDTO.getUtcnID() != null && !customerDTO.getUtcnID().isEmpty()) {
            final Optional<Customer> optionalCustomer = customerRepository.getByUtcnId(customerDTO.getUtcnID());

            if (optionalCustomer.isPresent()) {
                LOGGER.error("Customer with utcn id: {} already exist in database", customerDTO.getUtcnID());
                throw new CustomException(
                        "Clientul cu adresa de email " + customerDTO.getUtcnID() + " exista deja salvat in sistem",
                        HttpStatus.CONFLICT
                );
            }
        }

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
