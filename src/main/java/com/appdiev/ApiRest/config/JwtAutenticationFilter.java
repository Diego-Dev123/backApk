package com.appdiev.ApiRest.config;


import com.appdiev.ApiRest.models.User;
import com.appdiev.ApiRest.repositories.RoleRepository;
import com.appdiev.ApiRest.services.CustomUserDetailsService;
import com.appdiev.ApiRest.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAutenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final CustomUserDetailsService customUserDetailsService;

    private final RoleRepository roleRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authheader = request.getHeader("Authorization");

        if(authheader == null || !authheader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        String token = authheader.substring(7);

        try {
            String username = jwtUtil.extractUsername(token);

            if(username != null && SecurityContextHolder.getContext().getAuthentication() ==null){
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                if(jwtUtil.isTokenValid(token, userDetails)){
                    UsernamePasswordAuthenticationToken authtoken = new UsernamePasswordAuthenticationToken(userDetails,null,
                            roleRepository.findAllByUsersHasRoles_User_Id(((User) userDetails).getId()).stream()
                                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getId()))
                                    .toList());
                    authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authtoken);
                }
            }
        } catch (RuntimeException e) {
            // token vencido, malformado o usuario inexistente: sin autenticación -> 401 por el entry point
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request,response);

    }
}
