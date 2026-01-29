package me.moye.hew.database;

import me.moye.hew.model.PriceReductionNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PriceReductionNotificationRepository extends JpaRepository<PriceReductionNotification, Long>{
    Optional<PriceReductionNotification> getPriceReductionNotifiersByProductId(long productId);
}
