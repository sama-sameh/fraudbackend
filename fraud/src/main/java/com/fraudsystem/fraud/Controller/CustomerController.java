package com.fraudsystem.fraud.Controller;

import com.fraudsystem.fraud.Entity.Customer;
import com.fraudsystem.fraud.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private CustomerService customerService;
    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    @GetMapping("/all")
    public List<Customer> getAllCustomers() {
        return customerService.findAllCustomers();
    }
    @GetMapping("/{customerId}")
    public Customer getCustomerById(@PathVariable int customerId) {
        return customerService.findCustomerById(customerId);
    }
    @PostMapping("/add")
    public void addCustomer(@RequestBody Customer customer) {
        System.out.println(customer.getNationality_id());
        System.out.println(customer.getName());
        System.out.println(customer.getPhone_number());
        customerService.addCustomer(customer);
    }

}
