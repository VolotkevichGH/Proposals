package com.example.testjwt.controller;

import com.example.testjwt.entity.Proposal;
import com.example.testjwt.entity.RoleType;
import com.example.testjwt.entity.Status;
import com.example.testjwt.entity.User;
import com.example.testjwt.repositories.UserRepository;
import com.example.testjwt.services.ProposalService;
import com.example.testjwt.services.UserService;
import com.example.testjwt.web.ChangeStatusRequest;
import com.example.testjwt.web.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final ProposalService proposalService;
    private final UserRepository userRepository;
    private final UserService userService;


    @GetMapping("/proposals")
    public ResponseEntity<?> getProposals(@RequestParam int page, @RequestParam String typeSort){
        List<Proposal> proposals = new ArrayList<>();
        proposals.addAll(proposalService.findByStatus(Status.SENT));
        proposals.addAll(proposalService.findByStatus(Status.ACCEPTED));
        proposals.addAll(proposalService.findByStatus(Status.REJECTED));

        List<Proposal> nextProposals = proposalService.paginateProposals(proposals, page);

        return ResponseEntity.ok(proposalService.sortProposals(nextProposals, typeSort));
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@RequestParam int page){
        List<User> allUsers = userRepository.findAll();
        return ResponseEntity.ok(userService.paginateUsers(allUsers, page));
    }


    @GetMapping("/users/by/name")
    public ResponseEntity<?> getUsersByName(@RequestParam int page, @RequestParam String name){
        List<User> allUsersFromRepo = userRepository.findAll();
        List<User> allUsers = new ArrayList<>();

        for (User user:allUsersFromRepo){
            if (user.getName().contains(name)){
                allUsers.add(user);
            }
        }

        return ResponseEntity.ok(userService.paginateUsers(allUsers, page));
    }


    @PostMapping("/set/role/operator")
    public ResponseEntity<?> setOperator(@RequestBody ChangeStatusRequest changeStatusRequest){
        User user = userRepository.findById(changeStatusRequest.getId()).orElseThrow();
        return userService.setRole(user, RoleType.ROLE_OPERATOR);
    }

}
