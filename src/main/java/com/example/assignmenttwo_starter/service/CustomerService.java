package com.example.assignmenttwo_starter.service;

import com.example.assignmenttwo_starter.model.Customer;
import com.example.assignmenttwo_starter.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository cRepo;

//    public List<Customer> getAllCustomers() {
//        return (List<Customer>) cRepo.findAll();
//    }

    @Cacheable("customer-id")
    public Optional<Customer> findByID(Integer id) {
        return cRepo.findById(id);
    }

    @Cacheable("customer-all")
    public List<Customer> getAllCustomersPaged(int page, int size) {
        return cRepo.findAll(PageRequest.of(page, size)).getContent();
    }

    @CacheEvict(value = {"customer-id", "customer-all"}, allEntries = true)
    public void saveCustomer(Customer c) {
        cRepo.save(c);
    }

    @CacheEvict(value = {"customer-id", "customer-all"}, allEntries = true)
    public void deleteCustomer(Integer id) {
        cRepo.deleteById(id);
    }
}
