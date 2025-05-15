package com.team2.grabtable.domain.reservation.repository;

import com.team2.grabtable.domain.reservation.entity.ReservationSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationSlotRepository extends JpaRepository<ReservationSlot, Long> {
    List<ReservationSlot> findByStore_StoreId(Long storeId);
}
