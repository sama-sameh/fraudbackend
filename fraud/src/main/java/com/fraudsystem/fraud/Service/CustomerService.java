package com.fraudsystem.fraud.Service;

import com.fraudsystem.fraud.Entity.Customer;
import com.fraudsystem.fraud.Repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private CustomerRepository customerRepository;
    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    public Customer findCustomerById(int id) {
        return customerRepository.getById(id);
    }
    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }
    @Transactional
    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
    @Transactional
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
    @Transactional
    public void deleteCustomer(int id) {
        customerRepository.deleteById(id);
    }
    public Customer findCustomerByUserId(Long id){
        return customerRepository.findByUserId(id);
    }
    public Long getNumberOfCustomers() {
        return customerRepository.count();
    }
}
