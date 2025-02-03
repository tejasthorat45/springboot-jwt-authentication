package com.newspring.security.repo;

import com.newspring.security.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Customerrepo extends JpaRepository<Customer,Integer> {

    public Customer findByEmail(String email);



}
