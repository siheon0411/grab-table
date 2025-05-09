package com.team2.grabtable.domain.owner.entity;

import com.team2.grabtable.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ownerId;

    private String email;
    private String password;
    private String name;
    private Date birthdate;
    private Date createdAt;

    @OneToMany(mappedBy = "owner")
    private List<Store> storeList;
}
