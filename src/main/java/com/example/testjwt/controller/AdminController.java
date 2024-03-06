package com.example.testjwt.controller;

import com.example.testjwt.entity.Proposal;
import com.example.testjwt.entity.RoleType;
import com.example.testjwt.entity.Status;
import com.example.testjwt.entity.User;
import com.example.testjwt.repositories.UserRepository;
import com.example.testjwt.services.ProposalService;
import com.example.testjwt.web.ChangeStatusRequest;
import com.example.testjwt.web.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final ProposalService proposalService;
    private final UserRepository userRepository;


    @GetMapping("/proposals")
    public ResponseEntity<?> getProposals(@RequestParam int page){
        List<Proposal> proposals = new ArrayList<>();
        proposals.addAll(proposalService.findByStatus(Status.SENT));
        proposals.addAll(proposalService.findByStatus(Status.ACCEPTED));
        proposals.addAll(proposalService.findByStatus(Status.REJECTED));


        List<Proposal> nextProposals = new ArrayList<>();


        int finish = page*5;
        int start = finish-5;

        if (proposals.size() >= finish) {
            for (; start < finish; start++) {
                nextProposals.add(proposals.get(start));
            }
        } else {
            for (; start < proposals.size(); start++) {
                nextProposals.add(proposals.get(start));
            }
        }

        return ResponseEntity.ok(nextProposals);
    }

    @GetMapping("/proposals/reversed")
    public ResponseEntity<?> getProposalsReverse(@RequestParam int page){
        List<Proposal> proposals = new ArrayList<>();
        proposals.addAll(proposalService.findByStatus(Status.SENT));
        proposals.addAll(proposalService.findByStatus(Status.ACCEPTED));
        proposals.addAll(proposalService.findByStatus(Status.REJECTED));


        List<Proposal> nextProposals = new ArrayList<>();


        int finish = page*5;
        int start = finish-5;

        if (proposals.size() >= finish) {
            for (; start < finish; start++) {
                nextProposals.add(proposals.get(start));
            }
        } else {
            for (; start < proposals.size(); start++) {
                nextProposals.add(proposals.get(start));
            }
        }

        return ResponseEntity.ok(nextProposals.stream().map(Proposal::getDateOfCreate).toList().reversed());
    }

    @GetMapping("/proposals/sort")
    public ResponseEntity<?> getProposalsSort(@RequestParam int page){
        List<Proposal> proposals = new ArrayList<>();
        proposals.addAll(proposalService.findByStatus(Status.SENT));
        proposals.addAll(proposalService.findByStatus(Status.ACCEPTED));
        proposals.addAll(proposalService.findByStatus(Status.REJECTED));


        List<Proposal> nextProposals = new ArrayList<>();


        int finish = page*5;
        int start = finish-5;

        if (proposals.size() >= finish) {
            for (; start < finish; start++) {
                nextProposals.add(proposals.get(start));
            }
        } else {
            for (; start < proposals.size(); start++) {
                nextProposals.add(proposals.get(start));
            }
        }

        return ResponseEntity.ok(nextProposals.stream().map(Proposal::getDateOfCreate));
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@RequestParam int page){
        List<User> allUsers = userRepository.findAll();
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

        return ResponseEntity.ok(users);
    }


    @GetMapping("/users/by/name")
    public ResponseEntity<?> getUsersByName(@RequestParam int page, @RequestParam String name){
        List<User> allUsersFromRepo = userRepository.findAll();
        List<User> allUsers = new ArrayList<>();
        List<UserResponse> users = new ArrayList<>();

        for (User user:allUsersFromRepo){
            if (user.getName().contains(name)){
                allUsers.add(user);
            }
        }

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

        return ResponseEntity.ok(users);
    }


    @PostMapping("/set/role/operator")
    public ResponseEntity<?> setOperator(@RequestBody ChangeStatusRequest changeStatusRequest){
        User user = userRepository.findById(changeStatusRequest.getId()).orElseThrow();
        if (user.getRoles().contains(RoleType.ROLE_OPERATOR)){
            return ResponseEntity.badRequest().body("Пользователь уже является оператором!");
        }
        Set<RoleType> roles = user.getRoles();
        roles.add(RoleType.ROLE_OPERATOR);
        user.setRoles(roles);
        return ResponseEntity.ok(userRepository.save(user));
    }

}
