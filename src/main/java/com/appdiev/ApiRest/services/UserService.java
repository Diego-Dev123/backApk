package com.appdiev.ApiRest.services;

import com.appdiev.ApiRest.dto.role.RoleDTO;
import com.appdiev.ApiRest.dto.user.*;
import com.appdiev.ApiRest.dto.user.mapper.UserMapper;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service //hola
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

    @Autowired
    private UserMapper userMapper;


    @Transactional
    public LoginResponse create(CreateUserRequest request) {
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

    String token = jwtUtil.generateToken(user);

    List<Role> roles = roleRepository.findAllByUsersHasRoles_User_Id(savedUser.getId());

    LoginResponse response = new LoginResponse();
    response.setToken("Bearer " + token);
    response.setUser(userMapper.toUserResponse(user,roles));

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

        LoginResponse response = new LoginResponse();
        response.setToken("Bearer " + token);
        response.setUser(userMapper.toUserResponse(user,roles));

        return response;

    }



    @Transactional
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("email no valido"));

        List<Role> roles = roleRepository.findAllByUsersHasRoles_User_Id(user.getId());

        return userMapper.toUserResponse(user,roles);

    }

    //esto pa actualizar
    @Transactional
    public UserResponse update(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("usuario no encontrado"));

        if (request.getName() != null) user.setName(request.getName());
        if (request.getLastname() != null) user.setLastname(request.getLastname());
        if (request.getPhone() != null) user.setPhone(request.getPhone());

        userRepository.save(user);

        List<Role> roles = roleRepository.findAllByUsersHasRoles_User_Id(user.getId());
        return userMapper.toUserResponse(user, roles);
    }



    //-------------------------------------------------------------------------------
    @Transactional
    public UserResponse updateUserImage(Long id, UpdateUserRequest request) throws IOException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("email no valido"));

        //logica
        if (request.getName() != null){
            user.setName(request.getName());
        }
        if (request.getLastname() != null){
            user.setLastname(request.getLastname());
        }
        if (request.getPhone() != null){
            user.setPhone(request.getPhone());
        }

        if(request.getFile() != null && !request.getFile().isEmpty()){
            String uploadDir = "uploads/users/" + user.getId();
            String filename = request.getFile().getOriginalFilename();
            String filepath = Paths.get(uploadDir,filename).toString();

            Files.createDirectories(Paths.get(uploadDir));
            Files.copy(request.getFile().getInputStream(), Paths.get(filepath), StandardCopyOption.REPLACE_EXISTING);
            user.setImage("/" + filepath.replace("\\","/"));
        }

        userRepository.save(user);



        List<Role> roles = roleRepository.findAllByUsersHasRoles_User_Id(user.getId());

        return userMapper.toUserResponse(user,roles);

    }


    public void delete(Long id) {
    }
}



