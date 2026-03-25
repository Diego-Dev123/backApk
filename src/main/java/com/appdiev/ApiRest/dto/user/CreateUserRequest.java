package com.appdiev.ApiRest.dto.user;

import lombok.Data;

@Data
public class CreateUserRequest {
    public String name;
    public String lastname;
    public String email;
    public String phone;
    public String password;

}
