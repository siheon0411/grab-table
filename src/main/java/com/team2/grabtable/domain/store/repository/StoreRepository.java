package com.team2.grabtable.domain.store.repository;

import com.team2.grabtable.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("select s from Store s join fetch s.owner where s.owner.ownerId = :ownerId")
    List<Store> findByOwnerId(@Param("ownerId") Long ownerId);

//    @Query("select count(s) from Store s where s.owner.ownerId = :ownerId")
//    Long countByOwnerId(@Param("ownerId") Long ownerId);

}
