package com.deepak.userservice.services;

import com.deepak.userservice.models.User;

public interface IAuthService {
    User signup(User user);
}
