package com.team2.grabtable.domain.reservation.entity;

import com.team2.grabtable.domain.store.entity.Store;
import com.team2.grabtable.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Temporal(TemporalType.DATE)    // 시간 없이 날짜만 저장
    private Date visitDate;         // 방문할 날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id")
    private ReservationSlot reservationSlot;

    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt; // 예약 생성 시간

    private String status;  // 방문전 = before, 방문후(사장님체크) = after
}
