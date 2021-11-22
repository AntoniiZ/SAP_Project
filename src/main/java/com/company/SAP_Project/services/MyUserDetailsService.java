package com.company.SAP_Project.services;

import com.company.SAP_Project.models.UserModel;
import com.company.SAP_Project.repositories.UserRepository;
import com.company.SAP_Project.repositories.VerificationTokenRepository;
import com.company.SAP_Project.repositories.tables.User;
import com.company.SAP_Project.repositories.tables.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

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

    public User registerNewUserAccount(UserModel userModel) throws UserServiceException {

        if (emailExists(userModel.getEmail()))
        {
            throw new UserServiceException("The email is already registered - " + userModel.getEmail());
        }
        if (usernameExists(userModel.getUsername()))
        {
            throw new UserServiceException("The username is already registered - " + userModel.getUsername());
        }

        User user = new User();
        user.setUsername(userModel.getUsername());
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        user.setEmail(userModel.getEmail());
        user.setActive(false);
        user.setRoles("USER");

        return userRepository.save(user);
    }

    public void activate(User user)
    {
        user.setActive(true);
        userRepository.save(user);
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public User getUserByToken(String verificationToken) {
        return tokenRepository.findByToken(verificationToken).getUser();
    }

    public VerificationToken getVerificationToken(String verificationToken) {
        return tokenRepository.findByToken(verificationToken);
    }
    public void createVerificationToken(User user, String token) {
        tokenRepository.save(new VerificationToken(user, token));
    }
}