package com.example.testjwt.services;

import com.example.testjwt.entity.RoleType;
import com.example.testjwt.entity.User;
import com.example.testjwt.repositories.UserRepository;
import com.example.testjwt.web.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createNewAccount(User user, RoleType role){
        user.setRoles(Collections.singleton(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Username not found!"));
    }


    public List<UserResponse> paginateUsers(List<User> allUsers, int page){
        List<UserResponse> users = new ArrayList<>();
        int finish = page*5;
        int start = finish-5;

        if (allUsers.size() >= finish) {
            for (; start < finish; start++) {
                UserResponse userResponse = UserResponse.builder()
                        .id(allUsers.get(start).getId())
                        .name(allUsers.get(start).getName())
                        .phone(allUsers.get(start).getPhone())
                        .roles(allUsers.get(start).getRoles())
                        .username(allUsers.get(start).getUsername())
                        .build();
                users.add(userResponse);
            }
        } else {
            for (; start < allUsers.size(); start++) {
                UserResponse userResponse = UserResponse.builder()
                        .id(allUsers.get(start).getId())
                        .name(allUsers.get(start).getName())
                        .phone(allUsers.get(start).getPhone())
                        .roles(allUsers.get(start).getRoles())
                        .username(allUsers.get(start).getUsername())
                        .build();
                users.add(userResponse);
            }
        }
        return users;
    }

    public ResponseEntity<?> setRole (User user, RoleType roleType){
        if (user.getRoles().contains(roleType)){
            return ResponseEntity.badRequest().body("Пользователь уже является оператором!");
        }
        Set<RoleType> roles = user.getRoles();
        roles.add(roleType);
        user.setRoles(roles);
        return ResponseEntity.ok(userRepository.save(user));
    }

}
