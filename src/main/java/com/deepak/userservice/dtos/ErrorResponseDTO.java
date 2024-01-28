package com.deepak.userservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.ErrorResponse;

@Getter
@Setter
public class ErrorResponseDTO {
    private String message;
    private int httpStatus;
}
