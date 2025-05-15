package com.team2.grabtable.domain.review.repository;

import com.team2.grabtable.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("""
        select r
          from Review r
     join fetch r.menu m
     join fetch m.store
     join fetch r.user u
     left join fetch u.memberships
         where r.store.storeId = :storeId
        """)
    Page<Review> findByStoreId(@Param("storeId") Long storeId, Pageable pageable);

    Long countByStoreStoreId(Long storeId);

    @Query("""
        select r
          from Review r
     join fetch r.menu m
     join fetch m.store
     join fetch r.user u
     left join fetch u.memberships
         where m.menuId = :menuId
        """)
    List<Review> findByMenuId(@Param("menuId") Long menuId);

    @Query("""
        select r
          from Review r
     join fetch r.menu m
     join fetch m.store
     join fetch r.user u
     left join fetch u.memberships
         where r.store.storeId = :storeId
        """)
    List<Review> findByStoreId(@Param("storeId") Long storeId);
}
