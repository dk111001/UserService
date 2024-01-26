package com.deepak.userservice.dtos;

import com.deepak.userservice.models.Role;
import com.deepak.userservice.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String username;
    private String token;
    private Set<Role> roles = new HashSet<>();

    public static UserResponseDTO from(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setRoles(user.getRoles());
        userResponseDTO.setId(user.getId());
        return userResponseDTO;
    }
}
