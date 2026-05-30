package com.sha.backend.repository;

import com.sha.backend.domain.Holding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HoldingRepository extends JpaRepository<Holding, Long> {
    Optional<Holding> findByUserIdAndSymbol(Long userId, String symbol);
    List<Holding> findByUserId(Long userId);
}