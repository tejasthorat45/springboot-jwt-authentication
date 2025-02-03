package com.newspring.security.filter;

import com.newspring.security.service.Customerservice;
import com.newspring.security.service.Jwtservice;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class Appfilter extends OncePerRequestFilter {
    @Autowired
    private Jwtservice jwtservice;

    @Autowired
    private Customerservice customerservice;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = null;
        String username= null;
        String header = request.getHeader("Authorization");

        if(header!=null && header.startsWith("Bearer ")){ 
             token = header.substring(7);
           username = jwtservice.extractUsername(token);
        }
             //load username and check the authetication null (by defaukt is null
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){

            UserDetails userDetails = customerservice.loadUserByUsername(username);
            //validate the user detail  and token
            Boolean isvalid = jwtservice.validateToken(token, userDetails);
            // if token is valid and user is authenticated, set the authentication
            if(isvalid){
                //validate the  user authentication
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // set the authentication
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }



        }
        filterChain.doFilter(request, response); // continue with the request processing

        //

    }
}
