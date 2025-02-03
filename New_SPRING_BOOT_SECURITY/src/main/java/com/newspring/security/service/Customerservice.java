package com.newspring.security.service;
import com.newspring.security.entity.Customer;
import com.newspring.security.repo.Customerrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class Customerservice implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder bcryptPasswordEncoder;

    @Autowired
    private Customerrepo customerrepo;

    public boolean saveCustomer(Customer c){
        String encodedpwd = bcryptPasswordEncoder.encode(c.getPwd());
        c.setPwd(encodedpwd);

        Customer savedcustomer = customerrepo.save(c);
        return savedcustomer.getCid() != null;


    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerrepo.findByEmail(email);
        return new User(customer.getEmail(), customer.getPwd(), Collections.emptyList());

    }
}
