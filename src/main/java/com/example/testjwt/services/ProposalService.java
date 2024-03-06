package com.example.testjwt.services;

import com.example.testjwt.entity.Proposal;
import com.example.testjwt.entity.Status;
import com.example.testjwt.entity.User;
import com.example.testjwt.repositories.ProposalRepository;
import com.example.testjwt.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final UserRepository userRepository;

    public Proposal createProposal(User user, String message){
        Proposal proposal = Proposal.builder()
                .phone(user.getPhone())
                .name(user.getName())
                .message(message)
                .dateOfCreate(new Date())
                .status(Status.DRAFT)
                .build();

        List<Proposal> proposals = user.getProposals();
        proposals.add(proposal);
        Proposal proposal1 = proposalRepository.save(proposal);
        user.setProposals(proposals);
        userRepository.save(user);
        return proposal1;
    }

    public List<Proposal> findByStatus(Status status){
        List<Proposal> proposals = new ArrayList<>();
        for (Proposal proposal : proposalRepository.findAll()){
            if (proposal.getStatus().equals(status)){
                proposals.add(proposal);
            }
        }
        return proposals;
    }


    public ResponseEntity<?> sendToOperator(long id, User user){
        Proposal proposal = proposalRepository.findById(id).orElseThrow(RuntimeException::new);
        if (!proposal.getStatus().equals(Status.DRAFT)){
            return ResponseEntity.badRequest().body("Вы не можете отправить несколько раз на рассмотрение одну заявку!");
        }

        if (!user.getProposals().contains(proposal)){
            return ResponseEntity.notFound().build();
        }

        proposal.setStatus(Status.SENT);
        return ResponseEntity.ok(proposalRepository.save(proposal));
    }

    public ResponseEntity<?> changeStatus(long id, Status status){
        Proposal proposal = proposalRepository.findById(id).orElseThrow(RuntimeException::new);
        proposal.setStatus(status);
        return ResponseEntity.ok(proposalRepository.save(proposal));
    }


    public ResponseEntity<?> editProposal(Proposal proposal, String message){
        proposal.setMessage(message);
        return ResponseEntity.ok(proposalRepository.save(proposal));
    }




}
