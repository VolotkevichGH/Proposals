package com.example.testjwt.controller;

import com.example.testjwt.entity.Proposal;
import com.example.testjwt.entity.Status;
import com.example.testjwt.repositories.ProposalRepository;
import com.example.testjwt.repositories.UserRepository;
import com.example.testjwt.services.ProposalService;
import com.example.testjwt.web.ChangeStatusRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/operator")
@PreAuthorize("hasRole('OPERATOR')")
public class OperatorController {

    private final ProposalService proposalService;
    private final UserRepository userRepository;
    private final ProposalRepository proposalRepository;

    @GetMapping("/proposals")
    public ResponseEntity<?> getProposals(@RequestParam int page, @RequestParam String typeSort){
        ArrayList<Proposal> proposals = new ArrayList<>(proposalService.findByStatus(Status.SENT));
        List<Proposal> nextProposals = proposalService.paginateProposals(proposals,page);
        return ResponseEntity.ok(proposalService.sortProposals(nextProposals, typeSort));
    }


    @PostMapping("/proposal/accept")
    public ResponseEntity<?> accept(@RequestBody ChangeStatusRequest changeStatusRequest){
        Proposal proposal = proposalRepository.findById(changeStatusRequest.getId()).orElseThrow();
        if (proposal.getStatus() != Status.SENT){
            return ResponseEntity.badRequest().body("Вы не можете изменить данную заявку!");
        }



       return proposalService.changeStatus(changeStatusRequest.getId(), Status.ACCEPTED);
    }


    @PostMapping("/proposal/reject")
    public ResponseEntity<?> reject(@RequestBody ChangeStatusRequest changeStatusRequest){

        Proposal proposal = proposalRepository.findById(changeStatusRequest.getId()).orElseThrow();

        if (proposal.getStatus() != Status.SENT){
            return ResponseEntity.badRequest().body("Вы не можете изменить данную заявку!");
        }


        return proposalService.changeStatus(changeStatusRequest.getId(), Status.REJECTED);
    }


    @GetMapping("/proposal/by/id")
    public ResponseEntity<?> getProposalById(@RequestParam Long id){

        Proposal proposal = proposalRepository.findById(id).orElseThrow();
        if (!proposal.getStatus().equals(Status.SENT)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(proposal);
    }

    @GetMapping("/proposal/by/name")
    public ResponseEntity<?> getProposalByName(@RequestParam String name, @RequestParam int page){

        List<Proposal> proposalsStart = proposalRepository.findAll();

        List<Proposal> proposals = new ArrayList<>();

        for (Proposal proposal : proposalsStart){
            if (proposal.getName().contains(name)){
                proposals.add(proposal);
            }
        }
        List<Proposal> nextProposals = proposalService.paginateProposals(proposals,page);


        if (nextProposals.size() == 1){
            return ResponseEntity.ok(nextProposals.get(0));
        }
        if (nextProposals.size()>1){
            return ResponseEntity.ok(nextProposals);
        }


        return ResponseEntity.notFound().build();
    }





}
