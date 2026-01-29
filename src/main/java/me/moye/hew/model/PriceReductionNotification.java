package me.moye.hew.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 商品价格下降通知
 */
@Entity
@Table(name = "price_reduction_notification")
@Getter
@Setter
public class PriceReductionNotification {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    // primary key of Product
    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private boolean notificationEnabled;

    /**
     * percent(%)
     */
    @Column(nullable = false)
    private float decreaseRate;

    @Column(nullable = false)
    private String recipientEmail;

    @Override
    public String toString() {
        return String.format("product={productId=%d, notificationEnabled=%b, decreaseRate=%f, recipientEmail=%s}",
                this.productId, this.notificationEnabled, this.decreaseRate, this.recipientEmail);
    }
}
