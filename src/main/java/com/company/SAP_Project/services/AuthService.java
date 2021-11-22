package com.company.SAP_Project.services;

import com.company.SAP_Project.repositories.UserRepository;
import com.company.SAP_Project.repositories.tables.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthService implements AuthenticationProvider {

    @Autowired
    private UserRepository repository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        UsernamePasswordAuthenticationToken token = null;
        String username = (String) authentication.getPrincipal();
        String password = authentication.getCredentials().toString();
        //System.out.println(username +'-' + password);

        if(authenticateUser(username, password))
        {
            token = new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
        }
        return token;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    public Boolean authenticateUser(String username, String password)
    {
        User user = repository.findByUsername(username);

        if(user == null) return false;

        return user.getPassword().equals(password);
    }

    public static String getAuthUsername()
    {
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }
}