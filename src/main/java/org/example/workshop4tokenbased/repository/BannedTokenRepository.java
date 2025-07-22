package org.example.workshop4tokenbased.repository;

import org.example.workshop4tokenbased.entity.BannedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BannedTokenRepository extends JpaRepository<BannedToken, Long> {

    Optional<BannedToken> findByToken(String token);
}