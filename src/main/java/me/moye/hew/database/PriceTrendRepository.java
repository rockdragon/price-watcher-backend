package me.moye.hew.database;

import me.moye.hew.model.PriceTrend;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PriceTrendRepository extends JpaRepository<PriceTrend, Long> {
    Optional<PriceTrend> findTopByProductIdOrderByPriceDateDesc(long productId);

    List<PriceTrend> findByProductIdOrderByPriceDateAsc(long productId);
}
