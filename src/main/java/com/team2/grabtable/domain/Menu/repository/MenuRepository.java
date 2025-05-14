package com.team2.grabtable.domain.Menu.repository;

import com.team2.grabtable.domain.Menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("select m from Menu m join fetch m.store where m.store.storeId = :storeId")
    List<Menu> findByStoreId(@Param("storeId") Long storeId);

}
