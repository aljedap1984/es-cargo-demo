package com.example.myeventsourcing.customer;

import com.example.myeventsourcing.customer.model.Customer;
import com.example.myeventsourcing.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

/**
 * Created by Administrador on 16/03/2016.
 */

@SpringBootApplication
public class CustomerConfiguration implements CommandLineRunner {

    @Autowired
    private CustomerService customerService;

    @Override
    public void run(String... args) throws Exception {
        Customer c = new Customer();
        c.setName("pil");
        c.setBalance(new BigDecimal(2000));
        customerService.save(c);
    }
}
