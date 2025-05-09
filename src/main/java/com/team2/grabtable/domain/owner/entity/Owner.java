package com.team2.grabtable.domain.owner.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="owner")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ownerId;
    private String email;
    private String password;
    private String name;
    private LocalDate birthdate;
    @CreationTimestamp
    private LocalDateTime createdAt;



}
