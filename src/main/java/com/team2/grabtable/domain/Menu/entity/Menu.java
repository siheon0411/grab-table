package com.team2.grabtable.domain.Menu.entity;

import com.team2.grabtable.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    private String name;
    private int price;

    @Lob
    @Column(name = "image", columnDefinition = "BLOB")
    private byte[] image;

    private String imageContentType;
}
