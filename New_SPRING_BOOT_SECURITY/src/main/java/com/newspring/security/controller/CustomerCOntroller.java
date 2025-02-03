package com.newspring.security.controller;

import com.newspring.security.entity.Customer;
import com.newspring.security.service.Customerservice;
import com.newspring.security.service.Jwtservice;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.channels.ScatteringByteChannel;

@RestController
public class CustomerCOntroller {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Customerservice customerservice;
    @Autowired
    private Jwtservice jwtservice;


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Customer c){
        try {

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(c.getEmail(), c.getPwd());
            //verfy login
            Authentication authenticate = authenticationManager.authenticate(token);

            if (authenticate.isAuthenticated()) {
                String tokenn = jwtservice.generateToken(c.getEmail());
                return ResponseEntity.ok(tokenn);
                //return  new ResponseEntity<String> ("Welcome to world",HttpStatus.OK);

            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication Failed");
        }

    }

    @PostMapping("/register")
    public ResponseEntity<String> registercustomer(@RequestBody Customer c){

        boolean status = customerservice.saveCustomer(c);

        if(status){
            return new ResponseEntity<>("Sucessfully registered", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Failed to register", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/welcome")
    public String welcome() {
        return "welcome to tejas";
    }




}
