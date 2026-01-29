package me.moye.hew.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "price_trends")
@Getter
@Setter
public class PriceTrend {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    // primary key of Product
    @Column(nullable = false)
    private Long productId;

    // Price
    @Column(nullable = false)
    private long price;

    // price datetime (yyyyMMdd in int)
    @Column(nullable = false)
    private int priceDate;

    @Override
    public String toString() {
        return String.format("product={productId=%d, price=%d, priceDate=%d}", this.productId, this.price, this.priceDate);
    }
}
