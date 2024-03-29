package com.cg.service.auth.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterRequest {

    @NotEmpty(message = "Trường này không được để trống")
    private String fullName;

    @NotEmpty(message = "Trường này không được để trống")
    private String phoneNumber;

    @NotEmpty(message = "Trường này không được để trống")
    private String username;

    @NotEmpty(message = "Trường này không được để trống")
    private String password;

    @NotEmpty(message = "Trường này không được để trống")
    private String email;

}
