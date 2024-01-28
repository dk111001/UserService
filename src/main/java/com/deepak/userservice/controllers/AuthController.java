package com.deepak.userservice.controllers;

import com.deepak.userservice.dtos.UserRequestDTO;
import com.deepak.userservice.dtos.UserResponseDTO;
import com.deepak.userservice.dtos.ValidateRequestDTO;
import com.deepak.userservice.models.Session;
import com.deepak.userservice.models.SessionStatus;
import com.deepak.userservice.models.User;
import com.deepak.userservice.services.AuthService;
import com.deepak.userservice.services.SessionService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final SessionService sessionService;
    AuthController(AuthService authService,
                   SessionService sessionService) {
        this.authService = authService;
        this.sessionService = sessionService;
    }
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> signup(@RequestBody UserRequestDTO userRequestDTO) {
        try {
            User user =  authService.signup(userRequestDTO.getUsername(), userRequestDTO.getPassword());
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

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserRequestDTO userRequestDTO) {
        try {
            User user = authService.getUserByUsername(userRequestDTO.getUsername());
            if (Objects.isNull(user)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            if (!authService.passwordMatches(userRequestDTO.getPassword(), user.getPassword())) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            final String token = authService.generateJwtToken(user);
            Session session = new Session();
            session.setUser(user);
            session.setToken(token);
            session.setSessionStatus(SessionStatus.ACTIVE);
            session.setExpiringAt(300);
            sessionService.createSession(session);
            MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
            headers.add(HttpHeaders.SET_COOKIE, "auth-token:" + token);
            return new ResponseEntity<>(UserResponseDTO.from(user), headers, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/validate")
    ResponseEntity<SessionStatus> validate(@RequestBody ValidateRequestDTO validateRequestDTO) {
        try {
            Long userId = authService.extractUserId(validateRequestDTO.getToken());
            SessionStatus sessionStatus = sessionService.validate(validateRequestDTO.getToken(), userId);
            return new ResponseEntity<>(sessionStatus, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
