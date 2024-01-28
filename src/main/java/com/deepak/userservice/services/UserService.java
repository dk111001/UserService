package com.deepak.userservice.services;

import com.deepak.userservice.models.User;
import com.deepak.userservice.repositories.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserService implements IUserService {
    private final UserRepository userRepository;
    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public User getUserById(Long userId){
        return null;
    }
}
