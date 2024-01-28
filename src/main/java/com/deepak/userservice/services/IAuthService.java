package com.deepak.userservice.services;

import com.deepak.userservice.models.User;

public interface IAuthService {
    User signup(String username, String password);
    User getUserByUsername(String username);
    boolean passwordMatches(String password, String encodedPassword);

    String generateJwtToken(User user);
//    User login(String username, String password);
}
