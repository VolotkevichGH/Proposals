package com.example.testjwt.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {

    private Long id;
    private String token;
    private String refreshToken;
    private String username;
    private String name;
    private String phone;
    private List<String> roles;

}
