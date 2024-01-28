package com.deepak.userservice.services;

import com.deepak.userservice.models.User;

public interface IUserService {
    User getUserById(Long userId);
    User getUserByUsername(String username);
}
