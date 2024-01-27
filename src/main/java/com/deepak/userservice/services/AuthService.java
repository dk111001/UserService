package com.deepak.userservice.services;

import com.deepak.userservice.dtos.UserResponseDTO;
import com.deepak.userservice.models.User;
import com.deepak.userservice.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // BCryptEncoder :- using external library so need to include using @Bean in SpringSecurity
    AuthService(UserRepository userRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    public String encodePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }
    public User signup(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodePassword(password));
        return userRepository.save(user);
    }
    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    public boolean passwordMatches(String password, String encodedPassword) {
        return bCryptPasswordEncoder.matches(password, encodedPassword);
    }
}
