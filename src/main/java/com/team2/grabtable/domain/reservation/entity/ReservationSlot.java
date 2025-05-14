package com.team2.grabtable.domain.reservation.entity;

import com.team2.grabtable.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservation_slot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long slotId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    private String startTime;
    private String endTime;

    @Enumerated(EnumType.STRING)
    private MembershipLevel allowedMembership;

    public enum MembershipLevel {
        GOLD, SILVER, BRONZE
    }

}
