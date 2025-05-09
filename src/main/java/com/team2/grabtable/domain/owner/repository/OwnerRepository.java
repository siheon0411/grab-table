package com.team2.grabtable.domain.owner.repository;

import com.team2.grabtable.domain.owner.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByEmail(String email);
    boolean existsByEmail(String email);

}
