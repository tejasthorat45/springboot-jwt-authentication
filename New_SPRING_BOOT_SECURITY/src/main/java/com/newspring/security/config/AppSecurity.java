package com.newspring.security.config;

import com.newspring.security.filter.Appfilter;
import com.newspring.security.service.Customerservice;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AppSecurity {

    @Autowired
    private Customerservice customerservice;
    @Autowired
    private BCryptPasswordEncoder bcryptPasswordEncoder;

    @Autowired
    private Appfilter jwtFilter;

//    @Bean
//    public BCryptPasswordEncoder pwdEncoder(){
//
//        return new BCryptPasswordEncoder();
//    }

    //security filter chain
    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        //logic
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/register","/login").permitAll()
                                .anyRequest().authenticated()

                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);;

        return http.build();

    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setPasswordEncoder(bcryptPasswordEncoder);
        authProvider.setUserDetailsService(customerservice);
        return authProvider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();

    }

}
