package com.team2.grabtable.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "membership")
@Getter
@Setter
@ToString
public class Membership {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long membershipId;

    private String name;
}
