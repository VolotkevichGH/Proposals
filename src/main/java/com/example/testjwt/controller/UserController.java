package com.example.testjwt.controller;

import com.example.testjwt.entity.Proposal;
import com.example.testjwt.entity.Status;
import com.example.testjwt.entity.User;
import com.example.testjwt.repositories.ProposalRepository;
import com.example.testjwt.repositories.UserRepository;
import com.example.testjwt.services.ProposalService;
import com.example.testjwt.web.CreateProposalRequest;
import com.example.testjwt.web.EditProposalRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class UserController {


    private final ProposalService proposalService;
    private final UserRepository userRepository;
    private final ProposalRepository proposalRepository;

    @PostMapping("/create/proposal")
    public ResponseEntity<?> createProposal(@RequestBody CreateProposalRequest message){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).get();
        return ResponseEntity.ok(proposalService.createProposal(user, message.getMessage()));

    }

    @GetMapping("/proposals")
    public ResponseEntity<?> getProposals(@RequestParam int page, @RequestParam String typeSort){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        List<Proposal> proposals = proposalService.paginateProposals(user.getProposals(), page);
        return ResponseEntity.ok(proposalService.sortProposals(proposals, typeSort));
    }


    @PostMapping("/proposal/send")
    public ResponseEntity<?> sendProposal(@RequestParam Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).get();
        return proposalService.sendToOperator(id, user);
    }

    @PostMapping("/proposal/edit")
    public  ResponseEntity<?> editProposal(@RequestBody EditProposalRequest editProposalRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).get();

        Proposal proposal = proposalRepository.findById(editProposalRequest.getId()).orElseThrow();

        if (!user.getProposals().contains(proposal)) {
            return ResponseEntity.notFound().build();
        }

        if (!proposal.getStatus().equals(Status.DRAFT)) {
            return ResponseEntity.badRequest().body("Вы не можете редактировать отправленную заявку!");
        }

        return proposalService.editProposal(proposal, editProposalRequest.getMessage());
    }


}
