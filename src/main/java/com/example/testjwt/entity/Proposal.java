package com.example.testjwt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "proposals")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String message;
    private Date dateOfCreate;



    public int compareTo(Proposal employee) {
        return getDateOfCreate().compareTo(employee.getDateOfCreate());
    }


}
