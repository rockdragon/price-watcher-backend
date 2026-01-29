package me.moye.hew.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {
    public Product() {
        this.isMonitored = true;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // ASIN code
    @Column(nullable = false, unique = true)
    private String asin;
    // Url
    @Column(nullable = false, unique = true)
    private String pageUrl;
    // Product name
    @Column(nullable = false)
    private String name;
    // Photo Url
    @Column(nullable = false)
    private String photoUrl;
    // out of stock?
    @Column(nullable = false)
    private boolean outOfStock;
    // is monitor?
    @Column(nullable = false)
    private boolean isMonitored;

    @Override
    public String toString() {
        return String.format("product={asin=%s, outOfStock=%b, name=%s, isMonitored=%b, photo=%s}",
                this.asin, this.outOfStock, this.name, this.isMonitored, this.photoUrl);
    }
}
