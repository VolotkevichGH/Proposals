package com.example.testjwt.web;

import com.example.testjwt.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {
    private String username;
    private String name;
    private String phone;
    private Set<RoleType> roles;
    private String password;

}
