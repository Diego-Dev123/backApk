package com.appdiev.ApiRest.repositories;

import com.appdiev.ApiRest.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, String> {
    boolean existsByName(String name);

//    List<Role> findAllByUsersHasRoles_User_Id(Long idUser);
    @Query("SELECT r FROM Role r JOIN r.UsersHasRoles uhr WHERE uhr.user.id = :userId")
    List<Role> findAllByUsersHasRoles_User_Id(Long userId);
}
