package com.appdiev.ApiRest.dto.user.mapper;


import com.appdiev.ApiRest.config.APIConfig;
import com.appdiev.ApiRest.dto.role.RoleDTO;
import com.appdiev.ApiRest.dto.user.UserResponse;
import com.appdiev.ApiRest.models.Role;
import com.appdiev.ApiRest.models.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user, List<Role> roles){
        List<RoleDTO> rolesDTOS = roles.stream()
                .map(role -> new RoleDTO(role.getId(), role.getName(), role.getImage(), role.getRoute()))
                .toList();

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setLastname(user.getLastname());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRoles(rolesDTOS);

        if(user.getImage() != null){
            String imageUrl = APIConfig.BASE_URL + user.getImage();
            response.setImage(imageUrl);
        }

        return response;
    }

}
