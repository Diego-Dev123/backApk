package com.appdiev.ApiRest.dto.user;


import com.appdiev.ApiRest.dto.role.RoleDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CreateUserResponse {
    public Long id;
    public String name;
    public String lastname;
    public String email;
    public String phone;
    public String image;

    @JsonProperty("notification_token")
    public String notificationToken;

    List<RoleDTO> roles;
}
