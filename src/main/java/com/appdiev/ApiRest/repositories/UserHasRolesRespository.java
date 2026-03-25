package com.appdiev.ApiRest.repositories;

import com.appdiev.ApiRest.models.UserHasRoles;
import com.appdiev.ApiRest.models.id.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHasRolesRespository extends JpaRepository<UserHasRoles, UserRoleId> {

}
