package com.company.SAP_Project.services;

import com.company.SAP_Project.repositories.UserRepository;
import com.company.SAP_Project.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class AuthService implements AuthenticationProvider {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        UsernamePasswordAuthenticationToken token = null;
        String username = (String) authentication.getPrincipal();
        String password = authentication.getCredentials().toString();

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

        if(user == null || !user.isActive()) return false;

        return passwordEncoder.matches(password, user.getPassword());
    }

    public String getAuthUsername()
    {
        String authUsername = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken))
        {
            authUsername = authentication.getName();
        }
        return authUsername;
    }

    public boolean isAdmin()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof AnonymousAuthenticationToken))
        {
            return false;
        }

        User user = repository.findByUsername(authentication.getName());
        return user.isAdmin() || user.isOwner();
    }
    public boolean isOwner()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof AnonymousAuthenticationToken))
        {
            return false;
        }

        User user = repository.findByUsername(authentication.getName());
        return user.isOwner();
    }
    public boolean isAuthenticated()
    {
        return getAuthUsername() != null;
    }
}