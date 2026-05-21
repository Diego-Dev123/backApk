package com.appdiev.ApiRest.dto.user;


import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private UserResponse user;
}
