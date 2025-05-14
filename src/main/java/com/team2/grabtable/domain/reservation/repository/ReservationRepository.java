package com.team2.grabtable.domain.reservation.repository;

import com.team2.grabtable.domain.reservation.entity.Reservation;
import com.team2.grabtable.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH r.reservationSlot " +
            "JOIN FETCH r.store " +
            "WHERE r.store.storeId = :storeId")
    List<Reservation> findWithSlotAndStoreByStoreId(@Param("storeId") Long storeId);

}
