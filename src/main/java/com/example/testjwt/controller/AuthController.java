package com.example.testjwt.controller;

import com.example.testjwt.entity.User;
import com.example.testjwt.exception.AlreadyExistsException;
import com.example.testjwt.repositories.UserRepository;
import com.example.testjwt.security.SecurityService;
import com.example.testjwt.security.jwt.JwtUtils;
import com.example.testjwt.services.RefreshTokenService;
import com.example.testjwt.web.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final SecurityService securityService;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> authUser(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(securityService.authenticateUser(loginRequest));
    }

//    @PostMapping("/register")
//    public ResponseEntity<SimpleResponse> register(@RequestBody CreateUserRequest request){
//        if (userRepository.existsByUsername(request.getUsername())){
//            throw new AlreadyExistsException("Username already exists!");
//        }
//
//        if (userRepository.existsByPhone(request.getPhone())){
//            throw new AlreadyExistsException("Email already exists!");
//        }
//
//        securityService.register(request);
//        return ResponseEntity.ok(new SimpleResponse("User created!"));
//    }


//    @PostMapping("/refresh-token")
//    public ResponseEntity<RefreshTokenResponse> refreshTokenResponseResponseEntity (@RequestBody RefreshTokenRequest refreshTokenRequest){
//        return ResponseEntity.ok(securityService.refreshToken(refreshTokenRequest));
//    }

    @PostMapping("/logout")
    public ResponseEntity<SimpleResponse> logout (){
        Principal auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        securityService.logout(user);
        return ResponseEntity.ok(new SimpleResponse(user.getName() + " вышел из системы!"));

    }



}
