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

    AuthService(UserRepository userRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    public String encodePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }
    public User signup(User user) {
        return userRepository.save(user);
    }
}
