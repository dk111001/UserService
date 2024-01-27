package com.deepak.userservice.dtos;

import lombok.Getter;

@Getter
public class ValidateRequestDTO {
    private Long userId;
    private String token;
}
