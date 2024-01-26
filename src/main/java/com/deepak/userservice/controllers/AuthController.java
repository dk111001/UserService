package com.deepak.userservice.controllers;

import com.deepak.userservice.dtos.UserRequestDTO;
import com.deepak.userservice.dtos.UserResponseDTO;
import com.deepak.userservice.models.User;
import com.deepak.userservice.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> signup(@RequestBody UserRequestDTO userRequestDTO) {
        try {
            User reuqestUser = new User();
            reuqestUser.setPassword(authService.encodePassword(userRequestDTO.getPassword()));
            reuqestUser.setUsername(userRequestDTO.getUsername());
            User user =  authService.signup(reuqestUser);
            ResponseEntity<UserResponseDTO> response;
            if (Objects.isNull(user)) {
                response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            else {
                response = new ResponseEntity<>(UserResponseDTO.from(user), HttpStatus.OK);
            }
            return response;
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
