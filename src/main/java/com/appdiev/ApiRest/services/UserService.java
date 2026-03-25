package com.appdiev.ApiRest.services;

import com.appdiev.ApiRest.dto.role.RoleDTO;
import com.appdiev.ApiRest.dto.user.CreateUserRequest;
import com.appdiev.ApiRest.dto.user.CreateUserResponse;
import com.appdiev.ApiRest.dto.user.LoginRequest;
import com.appdiev.ApiRest.dto.user.LoginResponse;
import com.appdiev.ApiRest.models.Role;
import com.appdiev.ApiRest.models.User;
import com.appdiev.ApiRest.models.UserHasRoles;
import com.appdiev.ApiRest.repositories.RoleRepository;
import com.appdiev.ApiRest.repositories.UserHasRolesRespository;
import com.appdiev.ApiRest.repositories.UserRepository;
import com.appdiev.ApiRest.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserHasRolesRespository userHasRolesRespository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;


    @Transactional
    public CreateUserResponse create(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email)) {
            throw new RuntimeException("correo ya registrado");
        }
        User user = new User();
        user.setName(request.name);
        user.setLastname(request.lastname);
        user.setPhone(request.phone);
        user.setEmail(request.email);

        String encriptedPassword = passwordEncoder.encode(request.password);
        user.setPassword(encriptedPassword);

    User savedUser = userRepository.save(user);
    Role clientRole = roleRepository.findById("CLIENT").orElseThrow(
            () -> new RuntimeException("el rol de cliente no existe  ")
    );

    UserHasRoles userHasRoles = new UserHasRoles(savedUser, clientRole);
    userHasRolesRespository.save(userHasRoles);

    CreateUserResponse response = new CreateUserResponse();
    response.setId(savedUser.getId());
    response.setName(savedUser.getName());
    response.setLastname(savedUser.getLastname());
    response.setEmail(savedUser.getEmail());
    response.setPhone(savedUser.getPhone());
    response.setImage(savedUser.getImage());

    List<Role> roles = roleRepository.findAllByUsersHasRoles_User_Id(savedUser.getId());
    List<RoleDTO> rolesDTOS = roles.stream()
                    .map(role -> new RoleDTO(role.getId(), role.getName(), role.getImage(),role.getRoute()))
                            .toList();

    response.setRoles(rolesDTOS);

    return response;
    }

    @Transactional
    public LoginResponse login (LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("email no valido"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("passwwrod no valido ");
        }

        String token = jwtUtil.generateToken(user);
        List<Role> roles = roleRepository.findAllByUsersHasRoles_User_Id(user.getId());
        List<RoleDTO> rolesDTOS = roles.stream()
                .map(role -> new RoleDTO(role.getId(), role.getName(), role.getImage(),role.getRoute()))
                .toList();

        CreateUserResponse createUserResponse = new CreateUserResponse();
        createUserResponse.setId(user.getId());
        createUserResponse.setName(user.getName());
        createUserResponse.setLastname(user.getLastname());
        createUserResponse.setEmail(user.getEmail());
        createUserResponse.setPhone(user.getPhone());
        createUserResponse.setImage(user.getImage());
        createUserResponse.setRoles(rolesDTOS);

        LoginResponse response = new LoginResponse();
        response.setToken("Bearer " + token);
        response.setUser(createUserResponse);

        return response;

    }



    @Transactional
    public CreateUserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("email no valido"));

        List<Role> roles = roleRepository.findAllByUsersHasRoles_User_Id(user.getId());
        List<RoleDTO> rolesDTOS = roles.stream()
                .map(role -> new RoleDTO(role.getId(), role.getName(), role.getImage(), role.getRoute()))
                .toList();

        CreateUserResponse createUserResponse = new CreateUserResponse();
        createUserResponse.setId(user.getId());
        createUserResponse.setName(user.getName());
        createUserResponse.setLastname(user.getLastname());
        createUserResponse.setEmail(user.getEmail());
        createUserResponse.setPhone(user.getPhone());
        createUserResponse.setImage(user.getImage());
        createUserResponse.setRoles(rolesDTOS);

        return createUserResponse;

    }

}



