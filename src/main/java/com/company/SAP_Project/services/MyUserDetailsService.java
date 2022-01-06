package com.company.SAP_Project.services;

import com.company.SAP_Project.dtoObjects.UserDto;
import com.company.SAP_Project.repositories.UserRepository;
import com.company.SAP_Project.repositories.VerificationTokenRepository;
import com.company.SAP_Project.models.User;
import com.company.SAP_Project.models.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null) throw new UsernameNotFoundException("This username is not found!");
        return new MyUserDetails(user);
    }

    public User getUserByUsername(String username)  {
        return userRepository.findByUsername(username);
    }

    public List<User> getUsers() {
        List<User> users = userRepository.getUsers();
        if(users == null) users = new ArrayList<>();
        return users;
    }
    public User registerNewUserAccount(UserDto userDto) throws CustomServiceException {

        if (emailExists(userDto.getEmail()))
        {
            throw new CustomServiceException(
                    String.format("The email %s is already registered", userDto.getEmail())
            );
        }
        if (usernameExists(userDto.getUsername()))
        {
            throw new CustomServiceException(
                    String.format("The username %s is already registered", userDto.getUsername())
            );
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setActive(false);
        user.setRoles("USER");


        return userRepository.save(user);
    }

    public void activate(User user, Long tokenId)
    {
        user.setActive(true);
        tokenRepository.deleteById(tokenId);
        userRepository.save(user);
    }

    public void addAdmin(String username)
    {

        if(!usernameExists(username))
        {
            return;
        }
        userRepository.setUserRole(username, "ADMIN");
    }

    public void removeAdmin(String username)
    {
        if(!usernameExists(username))
        {
            return;
        }
        userRepository.setUserRole(username, "USER");
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public VerificationToken getVerificationToken(String verificationToken) {
        return tokenRepository.findByToken(verificationToken);
    }
    public void createVerificationToken(User user, String token) {
        tokenRepository.save(new VerificationToken(user, token));
    }
}